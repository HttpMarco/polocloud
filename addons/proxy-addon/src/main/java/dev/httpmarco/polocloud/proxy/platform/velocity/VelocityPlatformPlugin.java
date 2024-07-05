/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.proxy.platform.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.events.player.CloudPlayerSwitchServerEvent;
import dev.httpmarco.polocloud.proxy.config.Config;
import dev.httpmarco.polocloud.proxy.platform.velocity.command.ProxyCommand;
import dev.httpmarco.polocloud.proxy.platform.velocity.listener.PingListener;
import dev.httpmarco.polocloud.proxy.platform.velocity.listener.PlayerDisconnectListener;
import dev.httpmarco.polocloud.proxy.platform.velocity.listener.ServerPostConnectListener;
import dev.httpmarco.polocloud.proxy.platform.velocity.configuration.maintenance.VelocityMaintenanceProvider;
import dev.httpmarco.polocloud.proxy.platform.velocity.configuration.tablist.VelocityTablistHandler;
import lombok.Getter;

import javax.inject.Inject;

@Getter
@Plugin(id = "polocloud-proxy", name = "PoloCloud", version = "1.0.0", authors = "HttpMarco")
public final class VelocityPlatformPlugin {

    private final Config config = new Config();
    private final ProxyServer server;
    private VelocityTablistHandler velocityTablistHandler;
    private VelocityMaintenanceProvider velocityMaintenanceProvider;

    @Inject
    public VelocityPlatformPlugin(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        this.velocityTablistHandler = new VelocityTablistHandler(this);
        this.velocityMaintenanceProvider = new VelocityMaintenanceProvider();

        var eventManager = this.server.getEventManager();
        eventManager.register(this, new PingListener(this));
        eventManager.register(this, new ServerPostConnectListener(this));
        eventManager.register(this, new PlayerDisconnectListener(this));

        var eventNode = CloudAPI.instance().globalEventNode();
        eventNode.addListener(CloudPlayerSwitchServerEvent.class, it -> this.getServer().getPlayer(it.cloudPlayer().uniqueId()).ifPresent(player -> this.getVelocityTablistHandler().update(player)));

        var commandManager = this.server.getCommandManager();
        commandManager.register(commandManager.metaBuilder("proxy").build(), new ProxyCommand());
    }
}
