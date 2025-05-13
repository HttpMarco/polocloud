package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.instance.event.EventInstanceProvider;
import dev.httpmarco.polocloud.instance.group.ClusterInstanceGroupProvider;
import dev.httpmarco.polocloud.instance.grpc.GrpcInstance;
import dev.httpmarco.polocloud.instance.loader.PolocloudInstanceLoader;
import dev.httpmarco.polocloud.instance.service.ClusterServiceInstanceProvider;
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

    private final EventInstanceProvider eventProvider;
    private final ClusterInstanceGroupProvider groupProvider;
    private final ClusterServiceProvider serviceProvider;

    public PolocloudInstance(String[] args) {
        instance = this;

        this.client = new GrpcInstance();
        this.eventProvider = new EventInstanceProvider(this.client.channel());
        this.groupProvider = new ClusterInstanceGroupProvider(this.client.channel());
        this.serviceProvider = new ClusterServiceInstanceProvider(this.client.channel());

        try {
            var platformPath = Path.of(PolocloudEnvironment.read(PolocloudEnvironment.POLOCLOUD_SUITE_PLATFORM_PATH));
            var loader = PolocloudInstanceLoader.generate(platformPath, Boolean.parseBoolean(System.getenv().get(PolocloudEnvironment.POLOCLOUD_SERVICE_SEPARAT_CLASSLOADER.name())));

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
}
