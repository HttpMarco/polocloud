package dev.httpmarco.polocloud.sdk.java;

import java.net.URLClassLoader;

public final class PolocloudJavaSdkClasspath {

    public PolocloudJavaSdkClasspath() {
        ClassLoader classLoader = this.getClass().getClassLoader();

        // if sdk run in a plugin, the classloader is the plugin classloader
        if(classLoader instanceof URLClassLoader urlClassLoader) {



            return;
        }

        throw new RuntimeException("PolocloudJavaSdkClasspath can only be used in a plugin context with a URLClassLoader. Use -all libraries to run the SDK in a standalone application.");
    }
}
