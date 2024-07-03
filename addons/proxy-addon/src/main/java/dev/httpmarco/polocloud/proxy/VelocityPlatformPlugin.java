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

package dev.httpmarco.polocloud.proxy;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.proxy.command.ProxyCommand;
import dev.httpmarco.polocloud.proxy.config.Config;
import dev.httpmarco.polocloud.proxy.listener.PingListener;
import dev.httpmarco.polocloud.proxy.listener.ServerPostConnectListener;
import dev.httpmarco.polocloud.proxy.maintenance.MaintenanceManager;
import dev.httpmarco.polocloud.proxy.tablist.TablistManager;
import lombok.Getter;

import javax.inject.Inject;
import java.util.UUID;

@Getter
@Plugin(id = "polocloud-proxy", name = "PoloCloud", version = "1.0.0", authors = "HttpMarco")
public final class VelocityPlatformPlugin {

    public static final UUID SELF_ID = UUID.fromString(System.getenv("serviceId"));

    private final ProxyServer server;
    private Config config;
    private TablistManager tabManager;
    private MaintenanceManager maintenanceManager;

    @Inject
    public VelocityPlatformPlugin(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        this.config = new Config();

        this.tabManager = new TablistManager(this);
        this.maintenanceManager = new MaintenanceManager(this);

        var eventManager = this.server.getEventManager();
        eventManager.register(this, new ServerPostConnectListener(this));
        eventManager.register(this, new PingListener(this));

        var commandManager = this.server.getCommandManager();
        commandManager.register(commandManager.metaBuilder("proxy").build(), new ProxyCommand());
    }
}
