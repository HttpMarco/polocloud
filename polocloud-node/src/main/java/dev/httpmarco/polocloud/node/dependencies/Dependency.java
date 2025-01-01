package dev.httpmarco.polocloud.node.dependencies;

import dev.httpmarco.polocloud.api.Available;

import java.io.File;

public interface Dependency extends Available {

    File file();

    void prepare();

}
