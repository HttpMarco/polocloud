package dev.httpmarco.polocloud.runner;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.node.NodeService;
import dev.httpmarco.polocloud.api.properties.CloudProperty;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.JarFile;

public class Instance extends CloudAPI {

    private final InstanceClient client;

    public static void main(String[] args) {
        //start platform
        new Instance(args);
    }

    @SneakyThrows
    public Instance(String[] args) {
        var bootstrapPath = Path.of(Arrays.stream(args).filter(it -> it.startsWith("--bootstrap=")).map(it -> it.substring("--bootstrap=".length())).findFirst().orElse(null) + ".jar");

        this.client = new InstanceClient("127.0.0.1", 8192);

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


    @Override
    public NodeService nodeService() {
        //todo
        return null;
    }

    @Override
    public CloudGroupProvider groupProvider() {
        //todo
        return null;
    }

    @Override
    public CloudServiceProvider serviceProvider() {
        //todo
        return null;
    }

    @Override
    public PropertiesPool<CloudProperty<?>> globalProperties() {
        //todo
        return null;
    }
}
