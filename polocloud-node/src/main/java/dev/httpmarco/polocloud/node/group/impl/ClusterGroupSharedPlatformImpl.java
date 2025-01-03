package dev.httpmarco.polocloud.node.group.impl;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.platforms.Platform;

public final class ClusterGroupSharedPlatformImpl extends SharedPlatform {

    public ClusterGroupSharedPlatformImpl(String name, Version version, PlatformType type) {
        super(name, version, type);
    }

    public Platform platform() {
        return Node.instance().platformProvider().indexingShared(this);
    }
}
