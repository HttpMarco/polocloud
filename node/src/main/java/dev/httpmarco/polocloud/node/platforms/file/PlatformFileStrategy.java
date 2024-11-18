package dev.httpmarco.polocloud.node.platforms.file;

public enum PlatformFileStrategy {

    COPY_FROM_CLASSPATH_IF_NOT_EXISTS,

    // CREATE OR OVERWRITE EXISTING FILES
    DIRECT_CREATE,

    // if file exists, replace the content
    // if not, create the file
    APPEND_OR_REPLACE

}
