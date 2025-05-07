package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.instance.grpc.InstanceClient;
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
    private final InstanceClient client;

    public PolocloudInstance(String[] args) {
        instance = this;

        this.client = new InstanceClient();

        try {
            Path platformPath = Path.of(PolocloudEnvironment.read(PolocloudEnvironment.POLOCLOUD_SUITE_PLATFORM_PATH));
            var loader = new PolocloudInstanceLoader(platformPath);

            try (var jarFile = new JarFile(platformPath.toString())) {
                final var mainClass = Class.forName( jarFile.getManifest().getMainAttributes().getValue("Main-Class"), false, loader);

                final var thread = new Thread(() -> {
                    try {
                        mainClass.getMethod("main", String[].class).invoke(null, (Object) args);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }, "PoloCloud-Service-Thread");
                thread.setContextClassLoader(loader);
                thread.start();

                // todo testing element
                Runtime.getRuntime().addShutdownHook(new Thread(thread::interrupt));
            } catch (Exception exception) {
                exception.printStackTrace(System.err);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClusterServiceProvider serviceProvider() {
        return null;
    }

    @Override
    public ClusterGroupProvider groupProvider() {
        return null;
    }
}
