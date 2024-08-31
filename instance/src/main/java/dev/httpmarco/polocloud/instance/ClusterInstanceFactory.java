package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.instance.context.DefaultInstanceContext;
import dev.httpmarco.polocloud.instance.context.SeparateInstanceContext;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.util.Arrays;
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

        final var thread = new Thread(() -> {
            try (final var jar = new JarFile(file)) {

                if (jar.getManifest().getMainAttributes().containsKey(new java.util.jar.Attributes.Name(PREMAIN_CLASS))) {
                    var premainClass = Class.forName(jar.getManifest().getMainAttributes().getValue(PREMAIN_CLASS), true, contextClassloader);
                    premainClass.getMethod("premain", String.class, Instrumentation.class).invoke(null, null, ClusterPremain.INSTRUMENTATION);
                }

                if (jar.getManifest().getMainAttributes().containsKey(new java.util.jar.Attributes.Name(LAUNCHER_AGENT_CLASS))) {
                    var launcherAgentClass = Class.forName(jar.getManifest().getMainAttributes().getValue(LAUNCHER_AGENT_CLASS), true, contextClassloader);
                    launcherAgentClass.getMethod("premain", String.class, Instrumentation.class).invoke(null, null, ClusterPremain.INSTRUMENTATION);
                }

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
