package dev.httpmarco.polocloud.suite.components.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ComponentClassLoader extends URLClassLoader {

    private static final Logger log = LogManager.getLogger(ComponentClassLoader.class);

    public ComponentClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public ComponentClassLoader(File file) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, ClassLoader.getSystemClassLoader());
    }

    protected void bind(File file) {
        try {
            super.addURL(file.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> read(String name) {
        try {
            return super.loadClass(name);
        } catch (ClassNotFoundException e) {
            log.error("Could not load class {}", name, e);
        }
        return null;
    }
}
