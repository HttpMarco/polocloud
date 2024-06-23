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
import com.velocitypowered.api.event.connection.PostLoginEvent;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.runner.CloudInstance;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class PostLoginListener {

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        var player = event.getPlayer();
        if (CloudInstance.instance().self().isFull() && !player.hasPermission("polocloud.connect.bypass.maxplayers")) {
            player.disconnect(MiniMessage.miniMessage().deserialize("<red>This service is full!"));
        }

        CloudAPI.instance().playerProvider().register(player.getUniqueId(), player.getUsername());
    }
}
