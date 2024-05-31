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

package dev.httpmarco.polocloud.bungeecord;

import dev.httpmarco.polocloud.RunningPlatform;
import dev.httpmarco.polocloud.RunningProxyPlatform;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

public final class BungeeCordPlatform extends Plugin implements Listener {

    private final RunningPlatform runningPlatform = new RunningProxyPlatform(
            it -> ProxyServer.getInstance().getOnlineCount(),
            it -> registerServer(it.name(), it.hostname(), it.port()),
            it -> ProxyServer.getInstance().getServers().remove(it.name()));

    @Override
    public void onEnable() {
        var instance = ProxyServer.getInstance();
        instance.getConfigurationAdapter().getServers().clear();
        instance.getServers().clear();
        instance.getPluginManager().registerListener(this, this);

        this.runningPlatform.changeToOnline();
    }

    private void registerServer(String name, String hostname, int port) {
        var info = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(hostname, port), "PoloCloud Service", false);
        ProxyServer.getInstance().getServers().put(name, info);
    }


    @EventHandler
    public void handle(PreLoginEvent event) {
        if (ProxyServer.getInstance().getServers().isEmpty()) {
            event.setReason(TextComponent.fromLegacy("No fallback server available"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(ServerConnectEvent event) {
        var service = CloudAPI.instance().serviceProvider().filterService(ServiceFilter.LOWEST_FALLBACK);

        if (service.isEmpty()) {
            event.getPlayer().disconnect(TextComponent.fromLegacy("No fallback server available"));
            event.setCancelled(true);
            return;
        }

        var info = ProxyServer.getInstance().getServerInfo(service.get(0).name());

        if (info == null) {
            event.getPlayer().disconnect(TextComponent.fromLegacy("No fallback server available"));
            event.setCancelled(true);
            return;
        }
        System.out.println("found fallback: " + service.get(0).name());
        event.setTarget(info);
    }
}
