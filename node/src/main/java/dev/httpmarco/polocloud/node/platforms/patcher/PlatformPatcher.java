package dev.httpmarco.polocloud.node.platforms.patcher;

import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;

import java.io.File;

public interface PlatformPatcher {

    void patch(File serverFile, ClusterLocalServiceImpl clusterLocalService);

    String id();

}
