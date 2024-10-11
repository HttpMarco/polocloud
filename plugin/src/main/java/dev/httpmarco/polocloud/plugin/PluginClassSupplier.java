package dev.httpmarco.polocloud.plugin;

import dev.httpmarco.polocloud.api.ClassSupplier;
import dev.httpmarco.polocloud.api.CloudAPI;

public class PluginClassSupplier implements ClassSupplier {
    public PluginClassSupplier() {
        CloudAPI.classSupplier(this);
    }

    @Override
    public Class<?> classByName(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }
}
