package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.instance.context.DefaultInstanceContext;
import dev.httpmarco.polocloud.instance.context.SeparateInstanceContext;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.JarFile;

@UtilityClass
public final class ClusterInstanceFactory {

    @SneakyThrows
    public void startPlatform(String[] args) {
        final var file = Path.of(System.getenv("bootstrapFile")).toFile();
        final var context = Arrays.stream(args).anyMatch(it -> it.equalsIgnoreCase("--separateClassLoader")) ? new SeparateInstanceContext() : new DefaultInstanceContext();
        final var contextClassloader = context.context(file);

        final var thread = new Thread(() -> {
            try (final var jar = new JarFile(file)) {

                var mainClass = jar.getManifest().getMainAttributes().getValue("Main-Class");
                var main = Class.forName(mainClass, true, contextClassloader).getMethod("main", String[].class);
                var arguments = Arrays.stream(args).filter(it -> !it.equalsIgnoreCase("--separateClassLoader")).toArray(String[]::new);

                //  start platform
                main.invoke(null, (Object) arguments);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setContextClassLoader(contextClassloader);
        thread.start();
    }

}
