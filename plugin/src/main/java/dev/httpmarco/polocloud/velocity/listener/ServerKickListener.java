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

package dev.httpmarco.polocloud.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import dev.httpmarco.polocloud.velocity.VelocityPlatform;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;

import java.net.InetSocketAddress;

@AllArgsConstructor
public class ServerKickListener {

    private final VelocityPlatform platform;

    @Subscribe
    public void handelKick(KickedFromServerEvent event) {
        var fallback = CloudAPI.instance().serviceProvider().filterService(ServiceFilter.LOWEST_FALLBACK);
        if (fallback.isEmpty()) {
            event.setResult(KickedFromServerEvent.DisconnectPlayer.create(Component.text("No server available!")));
            return;
        }

        var registeredServer = fallback.get(0);
        event.setResult(KickedFromServerEvent.RedirectPlayer.create(
                platform.getServer().registerServer(new ServerInfo(registeredServer.name(), new InetSocketAddress(registeredServer.hostname(), registeredServer.port()))
        )));
    }
}
