package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import dev.httpmarco.polocloud.api.event.EventProvider;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.instance.group.ClusterInstanceGroupProvider;
import dev.httpmarco.polocloud.instance.grpc.GrpcInstance;
import dev.httpmarco.polocloud.instance.loader.PolocloudInstanceLoader;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.jar.JarFile;

@Getter
@Accessors(fluent = true)
public final class PolocloudInstance extends Polocloud {

    @Getter
    private static PolocloudInstance instance;
    private final GrpcInstance client;

    private final ClusterInstanceGroupProvider groupProvider;

    public PolocloudInstance(String[] args) {
        instance = this;

        this.client = new GrpcInstance();
        this.groupProvider = new ClusterInstanceGroupProvider(this.client.channel());

        try {
            Path platformPath = Path.of(PolocloudEnvironment.read(PolocloudEnvironment.POLOCLOUD_SUITE_PLATFORM_PATH));
            var loader = new PolocloudInstanceLoader(platformPath);

            try (var jarFile = new JarFile(platformPath.toString())) {
                var mainClass = Class.forName(jarFile.getManifest().getMainAttributes().getValue("Main-Class"), false, loader);
                new PolocloudPlatformInvoker(loader, mainClass, args);
            } catch (Exception exception) {
                exception.printStackTrace(System.err);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public ClusterServiceProvider serviceProvider() {
        return null;
    }

    @Override
    public EventProvider eventProvider() {
        return null;
    }
}
