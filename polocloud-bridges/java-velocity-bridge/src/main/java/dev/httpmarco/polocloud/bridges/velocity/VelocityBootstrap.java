package dev.httpmarco.polocloud.bridges.velocity;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import com.google.inject.Inject;

@Plugin(id = "polocloud-bridge", name = "Polocloud-Bridge", version = "1.0.0-SNAPSHOT",
        url = "https://github.com/HttpMarco/polocloud", description = "Apply platform features", authors = {"HttpMarco"})
public final class VelocityBootstrap {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityBootstrap(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }



}
