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

package dev.httpmarco.polocloud.proxy.platform.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.proxy.platform.velocity.VelocityPlatformPlugin;
import dev.httpmarco.polocloud.runner.CloudInstance;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;

@AllArgsConstructor
public class PingListener {

    private final VelocityPlatformPlugin platform;

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        if (!CloudInstance.instance().self().group().properties().property(GroupProperties.MAINTENANCE)) return;

        final var version = new ServerPing.Version(0, this.platform.getVelocityMaintenanceProvider().maintenance().versionName());
        event.setPing(event.getPing().asBuilder()
                .version(version)
                .description(MiniMessage.miniMessage().deserialize(this.platform.getVelocityMaintenanceProvider().maintenance().motd()))
                .build());
    }
}
