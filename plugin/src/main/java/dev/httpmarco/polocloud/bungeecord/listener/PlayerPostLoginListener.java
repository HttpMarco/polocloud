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

package dev.httpmarco.polocloud.bungeecord.listener;

import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.runner.CloudInstance;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class PlayerPostLoginListener implements Listener {

    @EventHandler
    public void handleServerConnect(PostLoginEvent event) {
        var player = event.getPlayer();
        if (CloudInstance.instance().self().isFull() && !player.hasPermission("polocloud.connect.bypass.maxplayers")) {
            player.disconnect(BungeeComponentSerializer.get().serialize(
                    MiniMessage.miniMessage().deserialize("<red>This service is full!")));
            return;
        }

        if (CloudInstance.instance().self().group().properties().has(GroupProperties.MAINTENANCE)) {
            var state = CloudInstance.instance().self().group().properties().property(GroupProperties.MAINTENANCE);

            if (state && !(player.hasPermission("polocloud.connect.bypass.maintenance"))) {
                player.disconnect(BungeeComponentSerializer.get().serialize(
                        MiniMessage.miniMessage().deserialize("<red>This service is in maintenance!")));
                return;
            }
        }
    }
}