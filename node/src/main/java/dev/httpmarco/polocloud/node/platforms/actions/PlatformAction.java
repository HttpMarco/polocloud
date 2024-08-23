package dev.httpmarco.polocloud.node.platforms.actions;

import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;

public interface PlatformAction {

    String propertyId();

    void run(ClusterLocalServiceImpl service);

}
