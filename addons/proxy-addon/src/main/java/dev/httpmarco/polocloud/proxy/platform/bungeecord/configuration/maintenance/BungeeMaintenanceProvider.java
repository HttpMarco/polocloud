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

package dev.httpmarco.polocloud.proxy.platform.bungeecord.configuration.maintenance;

import dev.httpmarco.polocloud.proxy.config.maintenance.Maintenance;
import dev.httpmarco.polocloud.proxy.config.maintenance.MaintenanceProvider;
import dev.httpmarco.polocloud.proxy.platform.bungeecord.configuration.Configuration;

public class BungeeMaintenanceProvider implements MaintenanceProvider {

    public final static String VERSION_NAME = "§4§lMaintenance";
    public final static String MOTD = "§b§lPoloCloud §8» §fgithub.com/HttpMarco/polocloud\n§c§lCURRENTLY UNDER MAINTENANCE";
    private final Maintenance maintenance;

    public BungeeMaintenanceProvider() {
        this.maintenance = Configuration.loadOrCreateDefault().getMaintenance();
    }

    @Override
    public Maintenance maintenance() {
        return this.maintenance;
    }
}
