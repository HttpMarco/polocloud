package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.instance.loader.PolocloudInstanceLoader;

import java.lang.reflect.InvocationTargetException;

public final class PolocloudPlatformInvoker extends Thread {

    private final Class<?> mainClass;
    private final String[] args;

    public PolocloudPlatformInvoker(PolocloudInstanceLoader loader, Class<?> mainClass, String[] args) {
        this.mainClass = mainClass;
        this.args = args;

        this.setName("PoloCloud-Service-Thread");
        this.setContextClassLoader(loader);
        this.start();
    }

    @Override
    public void run() {
        try {
            mainClass.getMethod("main", String[].class).invoke(null, (Object) args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
