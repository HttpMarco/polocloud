package dev.httpmarco.polocloud.runner;

import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.JarFile;

public class Instance {

    @SneakyThrows
    public static void main(String[] args) {
        //start platform


        var bootstrapPath = Path.of(Arrays.stream(args).filter(it -> it.startsWith("--bootstrap=")).map(it -> it.substring("--bootstrap=".length())).findFirst().orElse(null) + ".jar");

        RunnerBootstrap.LOADER.addURL(bootstrapPath.toUri().toURL());

        try (final var jar = new JarFile(bootstrapPath.toFile())) {
            final var mainClass = jar.getManifest().getMainAttributes().getValue("Main-Class");
            try {
                final var main = Class.forName(mainClass, true, RunnerBootstrap.LOADER).getMethod("main", String[].class);
                main.invoke(null, (Object) Arrays.copyOfRange(args, 2, args.length));
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
