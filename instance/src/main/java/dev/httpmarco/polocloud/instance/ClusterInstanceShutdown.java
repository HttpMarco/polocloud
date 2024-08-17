package dev.httpmarco.polocloud.instance;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ClusterInstanceShutdown {

    public void shutdown() {
        ClusterInstance.instance().client().close();
    }
}
