package dev.httpmarco.polocloud.suite.dependencies;

import java.io.File;

public interface Dependency {

    /**
     * The current local system path
     * @return the file
     */
    File file();

}
