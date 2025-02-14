package dev.httpmarco.polocloud.suite.components.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ComponentClassLoader extends URLClassLoader {

    public ComponentClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    protected void bind(File file) {
        try {
            super.addURL(file.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
