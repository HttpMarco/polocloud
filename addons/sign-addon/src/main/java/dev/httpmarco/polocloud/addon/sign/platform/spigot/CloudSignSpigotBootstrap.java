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

package dev.httpmarco.polocloud.addon.sign.platform.spigot;

import dev.httpmarco.polocloud.addon.sign.CloudSignService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CloudSignSpigotBootstrap extends JavaPlugin {

    @Override
    public void onEnable() {
        var signService = new CloudSignService(new CloudSignSpigotFactory());

        getCommand("cloudsign").setExecutor(new CloudSignSpigotCommand());

        Bukkit.getScheduler().runTaskTimer(this, signService::tick, 1, 0);
    }
}
