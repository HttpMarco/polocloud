package dev.httpmarco.polocloud.suite.platforms.factory;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.platforms.Platform;
import dev.httpmarco.polocloud.suite.platforms.PlatformVersion;

public interface PlatformFactory {

    void downloadPlatform(Platform platform, PlatformVersion version);

    void bindPlatform(ClusterService service);

}
