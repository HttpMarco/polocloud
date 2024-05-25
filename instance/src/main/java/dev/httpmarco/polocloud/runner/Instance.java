package dev.httpmarco.polocloud.runner;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.node.NodeService;
import dev.httpmarco.polocloud.api.properties.CloudProperty;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
import dev.httpmarco.polocloud.runner.groups.InstanceGroupProvider;
import dev.httpmarco.polocloud.runner.services.InstanceServiceProvider;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.JarFile;

@Getter
@Accessors(fluent = true)
public class Instance extends CloudAPI {

    public static void main(String[] args) {
        //start platform
        new Instance(args);
    }

    @Getter
    private static Instance instance;

    private final InstanceClient client;
    private final CloudGroupProvider groupProvider = new InstanceGroupProvider();
    private final CloudServiceProvider serviceProvider = new InstanceServiceProvider();

    @SneakyThrows
    public Instance(String[] args) {
        instance = this;

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
    public PropertiesPool<CloudProperty<?>> globalProperties() {
        //todo
        return null;
    }
}
