package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.api.platform.SharedPlatform;

public interface PlatformProvider {

    /**
     * Find the parent platform of shared instance
     *
     * @param sharedPlatform the shared platform
     */
    Platform indexingShared(SharedPlatform sharedPlatform);

    /**
     * Search for a platform with plain text
     *
     * @param platform the platform
     * @param version  the version
     * @return the platform map
     */
    PlatformVersion searchPlatformVersion(String platform, String version);

}
