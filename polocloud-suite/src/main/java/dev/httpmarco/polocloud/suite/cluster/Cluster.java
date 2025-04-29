package dev.httpmarco.polocloud.suite.cluster;

import dev.httpmarco.polocloud.api.Closeable;

public interface Cluster extends Closeable {

    String name();

}
