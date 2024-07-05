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

package dev.httpmarco.polocloud.proxy.platform.velocity.configuration.tablist;

import com.velocitypowered.api.proxy.Player;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.proxy.platform.velocity.VelocityPlatformPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class VelocityTablistHandler extends VelocityTablistProvider {

    public VelocityTablistHandler(VelocityPlatformPlugin platform) {
        super(platform);
    }

    public void addPlayer(Player player) {
        update(player);
    }

    public void update(Player player) {
        var cloudPlayer = CloudAPI.instance().playerProvider().find(player.getUniqueId());

        var header = formatPlaceholders(getTablist().header(), cloudPlayer);
        var footer = formatPlaceholders(getTablist().footer(), cloudPlayer);

        player.sendPlayerListHeader(MiniMessage.miniMessage().deserialize(header));
        player.sendPlayerListFooter(MiniMessage.miniMessage().deserialize(footer));
    }
}
