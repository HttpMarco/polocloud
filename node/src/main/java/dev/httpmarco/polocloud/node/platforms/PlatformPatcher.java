package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface PlatformPatcher {

    String patchId();

    CompletableFuture<Void> patch(ClusterGroup clusterGroup, File file);

}
