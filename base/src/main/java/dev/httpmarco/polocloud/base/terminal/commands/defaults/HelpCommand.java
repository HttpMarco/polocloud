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

package dev.httpmarco.polocloud.base.terminal.commands.defaults;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;

@Command(command = "help", aliases = {"?"}, description = "Help you with all commands")
public final class HelpCommand {

    @DefaultCommand
    public void handle() {
        for (var command : CloudBase.instance().terminal().commandService().commands()) {

            var commandInfo = command.getClass().getDeclaredAnnotation(Command.class);

            var aliases = "";

            if (commandInfo.aliases().length != 0) {
                aliases = " &2(&1" + String.join("&2, &1", commandInfo.aliases()) + "&2)";
            }

            CloudAPI.instance().logger().info("&3" + commandInfo.command() + aliases +" &2- &1" + commandInfo.description() + "&2.");
        }
    }
}
