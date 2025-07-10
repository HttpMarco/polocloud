package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.lib.PolocloudLib;
import dev.httpmarco.polocloud.launcher.lib.PolocloudLibNotFoundException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PolocloudProcess extends Thread {

    private final List<PolocloudLib> processLibs;

    public PolocloudProcess() {
        super("PolocloudProcess");
        setDaemon(false);

        this.processLibs = PolocloudLib.of(PolocloudParameters.REQUIRED_LIBS);
        this.processLibs.forEach(PolocloudLib::copyFromClasspath);
    }

    @Override
    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder()
                .inheritIO()
                .command(arguments());

        // copy all environment variables from the current process
        String version = System.getProperty(PolocloudParameters.VERSION_ENV_ID);
        if (version != null) {
            processBuilder.environment().put(PolocloudParameters.VERSION_ENV_ID, version);
        }

        try {
            Process process = processBuilder.start();
            process.waitFor();
            process.exitValue(); // Optional: can be used for logging or error handling
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }

    private List<String> arguments() {
        List<String> arguments = new ArrayList<>();
        String usedJava = System.getenv("java.home");

        PolocloudLib bootLib = processLibs.stream()
                .filter(lib -> PolocloudParameters.BOOT_LIB.equals(lib.name()))
                .findFirst()
                .orElseThrow(() -> new PolocloudLibNotFoundException(PolocloudParameters.BOOT_LIB));

        if (usedJava != null) {
            arguments.add(usedJava + "/bin/java");
        } else {
            arguments.add("java");
        }

        arguments.add(String.format("-javaagent:%s", bootLib.target()));

        arguments.add("-cp");
        String classpathSeparator = windowsProcess() ? ";" : ":";
        String classpath = processLibs.stream()
                .map(lib -> lib.target().toString())
                .collect(Collectors.joining(classpathSeparator));
        arguments.add(classpath);

        arguments.add(bootLib.mainClass());
        return arguments;
    }

    private boolean windowsProcess() {
        return System.getProperty("os.name").toLowerCase(Locale.getDefault()).contains("win");
    }
}
