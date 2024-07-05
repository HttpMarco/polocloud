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

import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.proxy.platform.velocity.VelocityPlatformPlugin;
import dev.httpmarco.polocloud.proxy.platform.velocity.configuration.Configuration;
import dev.httpmarco.polocloud.proxy.config.tablist.Tablist;
import dev.httpmarco.polocloud.proxy.config.tablist.TablistFormatter;
import lombok.Getter;

public abstract class VelocityTablistProvider implements TablistFormatter {

    public final static String HEADER = "\n          <gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>- <gray>Simplest and easiest CloudSystem          \n<gray>Spieler: <aqua>%onlinePlayers%<gray>/<aqua>%maxOnlinePlayers% <dark_gray>| <gray>Current Server: <aqua>%server%\n";
    public final static String FOOTER = "\n<gray>Github: <white>github.com/HttpMarco/PoloCloud\n<gray>Discord: <aqua>https://discord.gg/VHjnNBRFBe\n";
    @Getter
    private final Tablist tablist;
    private final VelocityPlatformPlugin platform;

    public VelocityTablistProvider(VelocityPlatformPlugin platform) {
        this.tablist = Configuration.loadOrCreateDefault().getTablist();

        this.platform = platform;
    }

    @Override
    public String formatPlaceholders(String template, CloudPlayer cloudPlayer) {
        return template
                .replace("%server%", cloudPlayer.currentServerName())
                .replace("%onlinePlayers%", String.valueOf(this.platform.getServer().getPlayerCount()))
                .replace("%maxOnlinePlayers%", String.valueOf(this.platform.getServer().getConfiguration().getShowMaxPlayers()));
    }
}
