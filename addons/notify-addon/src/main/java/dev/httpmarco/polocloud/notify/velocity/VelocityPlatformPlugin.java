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

package dev.httpmarco.polocloud.notify.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.notify.configuration.ConfigManager;
import dev.httpmarco.polocloud.notify.velocity.listener.CloudServiceOnlineListener;
import dev.httpmarco.polocloud.notify.velocity.listener.CloudServiceShutdownListener;
import dev.httpmarco.polocloud.notify.velocity.listener.CloudServiceStartListener;

import javax.inject.Inject;

@Plugin(id = "polocloud-notify", name = "PoloCloud", version = "1.0.0", authors = "HttpMarco")
public final class VelocityPlatformPlugin {

    private final ProxyServer server;

    @Inject
    public VelocityPlatformPlugin(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        //load configurations
        var configManager = new ConfigManager();
        var messagesConfiguration = configManager.getMessagesConfiguration();

        var node = CloudAPI.instance().globalEventNode();

        new CloudServiceOnlineListener(server, messagesConfiguration, node);
        new CloudServiceShutdownListener(server, messagesConfiguration, node);
        new CloudServiceStartListener(server, messagesConfiguration, node);
    }
}
