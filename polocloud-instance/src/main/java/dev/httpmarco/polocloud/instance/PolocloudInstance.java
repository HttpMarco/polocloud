package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.instance.loader.PolocloudInstanceLoader;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.jar.JarFile;

public final class PolocloudInstance extends Polocloud {

    public PolocloudInstance() {


        try {
            Path platformPath = Path.of(PolocloudEnvironment.read(PolocloudEnvironment.POLOCLOUD_SUITE_PLATFORM_PATH));
            var loader = new PolocloudInstanceLoader(platformPath);
            var arguments = new ArrayList<>();

            try (var jarFile = new JarFile(platformPath.toString())) {
                final var mainClass = Class.forName( jarFile.getManifest().getMainAttributes().getValue("Main-Class"), false, loader);

                final var thread = new Thread(() -> {
                    try {
                        mainClass.getMethod("main", String[].class).invoke(null, (Object) arguments.toArray(new String[0]));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }, "PoloCloud-Service-Thread");
                thread.setContextClassLoader(loader);
                thread.start();
            } catch (Exception exception) {
                exception.printStackTrace(System.err);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        while (true) {

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
