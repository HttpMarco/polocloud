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

package dev.httpmarco.polocloud.proxy.tablist;

import com.velocitypowered.api.proxy.Player;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.proxy.VelocityPlatformPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.concurrent.ExecutionException;

public class TablistManager {

    private final VelocityPlatformPlugin platform;
    private final TablistConfiguration configuration;

    public TablistManager(VelocityPlatformPlugin platform) {
        (this.configuration = new TablistConfiguration(platform, null)).load();
        this.platform = platform;
    }

    public void addPlayer(Player player) {
        var cloudPlayer = CloudAPI.instance().playerProvider().find(player.getUniqueId());
        var server = platform.getServer().getServer(cloudPlayer.currentServerName());

        var header = replaceCommonPlaceholders(configuration.getTablist().getHeader(), player, cloudPlayer);
        var footer = replaceCommonPlaceholders(configuration.getTablist().getFooter(), player, cloudPlayer);

        if (server.isPresent()) {
            try {
                var ping = server.get().ping().get();
                var serverMotd = ping.getDescriptionComponent();

                header = replaceServerPlaceholders(header, MiniMessage.miniMessage().serialize(serverMotd));
                footer = replaceServerPlaceholders(footer, MiniMessage.miniMessage().serialize(serverMotd));
            } catch (ExecutionException | InterruptedException e) {
                CloudAPI.instance().logger().error("Error while replacing server placeholders", e);
            }
        }

        player.sendPlayerListHeader(MiniMessage.miniMessage().deserialize(header));
        player.sendPlayerListFooter(MiniMessage.miniMessage().deserialize(footer));
    }

    //TODO update method where ping and onlinePlayers get updated

    private String replaceCommonPlaceholders(String template, Player player, CloudPlayer cloudPlayer ) {
        return template
                .replace("%server%", cloudPlayer.currentServerName())
                .replace("%onlinePlayers%", String.valueOf(this.platform.getServer().getPlayerCount()))
                .replace("%maxOnlinePlayers%", String.valueOf(this.platform.getServer().getConfiguration().getShowMaxPlayers()))
                .replace("%proxy_motd%", MiniMessage.miniMessage().serialize(this.platform.getServer().getConfiguration().getMotd()))
                .replace("%ping%", String.valueOf(player.getPing()));
    }

    private String replaceServerPlaceholders(String template, String serverMotd) {
        return template
                .replace("%server_motd%", serverMotd);
    }
}
