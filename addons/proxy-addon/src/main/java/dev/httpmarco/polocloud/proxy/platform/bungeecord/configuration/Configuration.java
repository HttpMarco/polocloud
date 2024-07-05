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

package dev.httpmarco.polocloud.proxy.platform.bungeecord.configuration;

import dev.httpmarco.polocloud.proxy.config.Config;
import dev.httpmarco.polocloud.proxy.config.ProxyConfig;
import dev.httpmarco.polocloud.proxy.config.maintenance.Maintenance;
import dev.httpmarco.polocloud.proxy.platform.bungeecord.configuration.maintenance.BungeeMaintenanceProvider;
import dev.httpmarco.polocloud.proxy.platform.bungeecord.configuration.tablist.BungeeTablistProvider;
import dev.httpmarco.polocloud.proxy.config.tablist.Tablist;

public class Configuration {

    public static ProxyConfig loadOrCreateDefault() {
        var config = Config.load(ProxyConfig.class);
        if (config == null) {
            config = new ProxyConfig(new Maintenance(BungeeMaintenanceProvider.VERSION_NAME, BungeeMaintenanceProvider.MOTD), new Tablist(BungeeTablistProvider.HEADER, BungeeTablistProvider.FOOTER));
            Config.save(config);
            return config;
        }

        var versionName = (config.getMaintenance() == null) ? BungeeMaintenanceProvider.VERSION_NAME : (config.getMaintenance().versionName() == null) ? BungeeMaintenanceProvider.VERSION_NAME : config.getMaintenance().versionName();
        var motd = (config.getMaintenance() == null) ? BungeeMaintenanceProvider.MOTD : (config.getMaintenance().motd() == null) ? BungeeMaintenanceProvider.MOTD : config.getMaintenance().motd();

        var header = (config.getTablist() == null) ? BungeeTablistProvider.HEADER : (config.getTablist().header() == null) ? BungeeTablistProvider.HEADER : config.getTablist().header();
        var footer = (config.getTablist() == null) ? BungeeTablistProvider.FOOTER : (config.getTablist().footer() == null) ? BungeeTablistProvider.FOOTER : config.getTablist().footer();

        config.setMaintenance(new Maintenance(versionName, motd));
        config.setTablist(new Tablist(header, footer));

        Config.save(config);
        return config;
    }
}
