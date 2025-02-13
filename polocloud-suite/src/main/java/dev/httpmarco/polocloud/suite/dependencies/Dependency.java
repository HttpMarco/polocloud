package dev.httpmarco.polocloud.suite.dependencies;

import dev.httpmarco.polocloud.api.Named;

import java.io.File;

public interface Dependency extends Named {

    /**
     * The current local system path
     * @return the file
     */
    File file();

}
