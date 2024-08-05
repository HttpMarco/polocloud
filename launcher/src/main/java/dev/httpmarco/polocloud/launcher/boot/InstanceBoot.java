package dev.httpmarco.polocloud.launcher.boot;

import java.io.File;

public final class InstanceBoot extends AbstractBoot {

    @Override
    public void dependencyLoading() {

    }

    @Override
    public File bootFile() {
        return new File(System.getenv("bootstrapFile"));
    }
}
