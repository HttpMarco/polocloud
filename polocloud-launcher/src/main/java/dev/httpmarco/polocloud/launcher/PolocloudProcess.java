package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.exceptions.LibNotFoundException;
import dev.httpmarco.polocloud.launcher.lib.Lib;
import dev.httpmarco.polocloud.launcher.utils.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class PolocloudProcess extends Thread {

    private final LinkedList<Lib> processLibs;

    public PolocloudProcess() {
        this.processLibs = Lib.of(Parameters.REQUIRED_LIBS);
        this.processLibs.forEach(Lib::copyFromClasspath);
    }

    @Override
    public void run() {
        try {
            var process = new ProcessBuilder().inheritIO().command(processArguments()).start();
            // wait for the end of the polocloud suite
            process.waitFor();
        } catch (InterruptedException e) {
            this.interrupt();
        } catch (IOException | LibNotFoundException exception) {
            exception.printStackTrace(System.err);
        }
    }

    private List<String> processArguments() throws LibNotFoundException {
        var arguments = new ArrayList<String>();

        var bootLib = this.processLibs.stream()
                .filter(it -> it.name().equals(Parameters.BOOT_LIB))
                .findFirst()
                .orElseThrow(() -> new LibNotFoundException(Parameters.BOOT_LIB));

        arguments.add("java");
        arguments.add("-javaagent:%s".formatted(bootLib.target()));

        arguments.add("-cp");
        arguments.add(String.join(";", processLibs.stream().map(it -> it.target().toString()).toList()));
        arguments.add(bootLib.mainClass());

        return arguments;
    }
}
