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

package dev.httpmarco.polocloud.proxy.platform.velocity.configuration;

import dev.httpmarco.polocloud.proxy.config.Config;
import dev.httpmarco.polocloud.proxy.config.ProxyConfig;
import dev.httpmarco.polocloud.proxy.config.maintenance.Maintenance;
import dev.httpmarco.polocloud.proxy.platform.velocity.configuration.maintenance.VelocityMaintenanceProvider;
import dev.httpmarco.polocloud.proxy.platform.velocity.configuration.tablist.VelocityTablistProvider;
import dev.httpmarco.polocloud.proxy.config.tablist.Tablist;

public class Configuration {

    public static ProxyConfig loadOrCreateDefault() {
        var config = Config.load(ProxyConfig.class);
        if (config == null) {
            config = new ProxyConfig(new Maintenance(VelocityMaintenanceProvider.VERSION_NAME, VelocityMaintenanceProvider.MOTD), new Tablist(VelocityTablistProvider.HEADER, VelocityTablistProvider.FOOTER));
            Config.save(config);
            return config;
        }

        var versionName = (config.getMaintenance() == null) ? VelocityMaintenanceProvider.VERSION_NAME : (config.getMaintenance().versionName() == null) ? VelocityMaintenanceProvider.VERSION_NAME : config.getMaintenance().versionName();
        var motd = (config.getMaintenance() == null) ? VelocityMaintenanceProvider.MOTD : (config.getMaintenance().motd() == null) ? VelocityMaintenanceProvider.MOTD : config.getMaintenance().motd();

        var header = (config.getTablist() == null) ? VelocityTablistProvider.HEADER : (config.getTablist().header() == null) ? VelocityTablistProvider.HEADER : config.getTablist().header();
        var footer = (config.getTablist() == null) ? VelocityTablistProvider.FOOTER : (config.getTablist().footer() == null) ? VelocityTablistProvider.FOOTER : config.getTablist().footer();

        config.setMaintenance(new Maintenance(versionName, motd));
        config.setTablist(new Tablist(header, footer));

        Config.save(config);
        return config;
    }
}