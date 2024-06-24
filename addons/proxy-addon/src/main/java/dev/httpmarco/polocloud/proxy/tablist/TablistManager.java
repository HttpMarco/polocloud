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

    public final static String HEADER = "\n          <gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>- <gray>Simplest and easiest CloudSystem          \n<gray>Current Server: <aqua>%server%\n";
    public final static String FOOTER = "\n<gray>Github: <white>github.com/HttpMarco/PoloCloud\n<gray>Discord: <aqua>https://discord.gg/VHjnNBRFBe";
    private final VelocityPlatformPlugin platform;
    private final Tablist tablist;

    public TablistManager(VelocityPlatformPlugin platform) {
        this.platform = platform;
        this.tablist = this.platform.getConfig().loadOrCreateDefault().getTablist();
    }

    public void addPlayer(Player player) {
        var cloudPlayer = CloudAPI.instance().playerProvider().find(player.getUniqueId());
        var server = platform.getServer().getServer(cloudPlayer.currentServerName());

        var header = replaceCommonPlaceholders(this.tablist.header(), player, cloudPlayer);
        var footer = replaceCommonPlaceholders(this.tablist.footer(), player, cloudPlayer);

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
