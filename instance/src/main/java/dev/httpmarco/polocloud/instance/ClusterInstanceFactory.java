package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.instance.context.DefaultInstanceContext;
import dev.httpmarco.polocloud.instance.context.SeparateInstanceContext;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@UtilityClass
public final class ClusterInstanceFactory {

    private static final String PREMAIN_CLASS = "Premain-Class";
    private static final String LAUNCHER_AGENT_CLASS = "Launcher-Agent-Class";

    @SneakyThrows
    public void startPlatform(String[] args) {
        final var file = Path.of(System.getenv("bootstrapFile")).toFile();
        final var context = Arrays.stream(args).anyMatch(it -> it.equalsIgnoreCase("--separateClassLoader")) ? new SeparateInstanceContext() : new DefaultInstanceContext();
        final var contextClassloader = context.context(file);

        // append system libs for class loading problems (fabric, sponge)
        System.setProperty("fabric.systemLibraries", System.getProperty("java.class.path"));

        final var thread = new Thread(() -> {
            try (final var jar = new JarFile(file)) {
                preClassCall(jar, PREMAIN_CLASS, contextClassloader);
                preClassCall(jar, LAUNCHER_AGENT_CLASS, contextClassloader);

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

    @SneakyThrows
    private void preClassCall(@NotNull JarFile jarFile, String attribute, ClassLoader loader) {
        if (jarFile.getManifest().getMainAttributes().containsKey(new java.util.jar.Attributes.Name(attribute))) {
            var preClass = Class.forName(jarFile.getManifest().getMainAttributes().getValue(attribute), true, loader);
            preClass.getMethod("premain", String.class, Instrumentation.class).invoke(null, null, ClusterPremain.INSTRUMENTATION);
        }
    }
}
