package dev.httpmarco.polocloud.plugin.sponge;

import com.google.inject.Inject;
import dev.httpmarco.polocloud.api.ClassSupplier;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.plugin.PluginPlatform;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("Polocloud-plugin")
public final class SpongePlatformBootstrap implements ClassSupplier {

    private final PluginPlatform platform;

    @Inject
    SpongePlatformBootstrap() {
        this.platform = new PluginPlatform();
    }

    @Listener
    private void onConstructPlugin2(final StartedEngineEvent<Server> event) {
        platform.presentServiceAsOnline();

        CloudAPI.classSupplier(this);
    }

    @Override
    public Class<?> classByName(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }
}
