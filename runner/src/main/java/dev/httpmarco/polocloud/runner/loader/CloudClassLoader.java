package dev.httpmarco.polocloud.runner.loader;

import java.net.URL;
import java.net.URLClassLoader;

public final class CloudClassLoader extends URLClassLoader {

    public CloudClassLoader() {
        super(new URL[0], ClassLoader.getSystemClassLoader());
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
