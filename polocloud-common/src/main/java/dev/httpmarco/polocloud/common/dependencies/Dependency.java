package dev.httpmarco.polocloud.common.dependencies;

import java.io.File;

public interface Dependency {

    File jarFile();

    void prepare();

}
