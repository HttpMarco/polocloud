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
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PlayerChooseInitialServerListener {
    private final ProxyServer server;

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        var service = CloudAPI.instance().serviceProvider().filterService(ServiceFilter.LOWEST_FALLBACK);
        if (service.isEmpty()) {
            event.setInitialServer(null);
            return;
        }

        this.server.getServer(service.get(0).name()).ifPresentOrElse(event::setInitialServer, () -> event.setInitialServer(null));
    }
}
