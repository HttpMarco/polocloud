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

package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;

@Command(command = "node", aliases = {"nodes"}, description = "Manage all your nodes or connect other ones.")
public final class NodeCommand {

    @DefaultCommand
    public void handle() {
        var logger = CloudAPI.instance().logger();
        logger.info("node list - list all nodes");
        logger.info("node add <hostname> <port> - node ");
        logger.info("node remove <name> - test description");
        logger.info("node status- test description");
    }

    @SubCommand(args = {"list"})
    public void list() {
        CloudAPI.instance().logger().info("polo");
    }
}