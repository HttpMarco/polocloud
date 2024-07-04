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
import dev.httpmarco.polocloud.api.CloudAPI;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CloudSignSpigotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        var player = (Player) commandSender;

        if (args.length == 2 && args[0].equals("add")) {
            var groupName = args[1];

            if (!CloudAPI.instance().groupProvider().isGroup(groupName)) {
                player.sendMessage("This group does not exists");
                return false;
            }

            var possibleSignBlock = player.getTargetBlockExact(8);

            if (possibleSignBlock == null) {
                player.sendMessage("You must look at a wall sign block");
                return false;
            }

            if (!possibleSignBlock.getType().data.equals(WallSign.class)) {
                player.sendMessage("The detected block is not a wall sign");
                return false;
            }

            CloudSignService.instance().registerSign(groupName, possibleSignBlock.getWorld().getName(), possibleSignBlock.getX(), possibleSignBlock.getY(), possibleSignBlock.getZ());
            player.sendMessage("Set successfully sign");
            return false;
        }

        if (args.length == 1 && args[0].equals("remove")) {
            var possibleSignBlock = player.getTargetBlockExact(8);


            if (possibleSignBlock == null) {
                player.sendMessage("You must look at a wall sign block");
                return false;
            }

            if (!possibleSignBlock.getType().data.equals(WallSign.class)) {
                player.sendMessage("The detected block is not a wall sign");
                return false;
            }

            //todo
            player.sendMessage("Remove successfully sign");
            return false;
        }

        player.sendMessage("cloudsign add <name>");
        player.sendMessage("cloudsign remove");
        return false;
    }
}
