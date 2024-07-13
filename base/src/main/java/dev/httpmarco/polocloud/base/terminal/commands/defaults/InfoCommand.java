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

@Command(command = "info", aliases = {"me"})
public class InfoCommand {

    @DefaultCommand
    public void handle() {

        var thisNode = CloudAPI.instance().nodeService().localNode();

        //todo
        CloudAPI.instance().logger().info("Self node Id&2: &3" + thisNode.name());
        CloudAPI.instance().logger().info("Max node memory&2: &3" + CloudBase.instance().cloudConfiguration().content().maxMemory());
        CloudAPI.instance().logger().info("Registered groups&2: &3" + CloudBase.instance().groupProvider().groups().size());
        CloudAPI.instance().logger().info("Online services&2: &3" + CloudBase.instance().serviceProvider().services().size());
        CloudAPI.instance().logger().info("Amount of commands&2: &3" + CloudBase.instance().terminal().commandService().commands().size());

        var runtime = Runtime.getRuntime();
        int mb = 1024 * 1024;
        var currentMemory = runtime.totalMemory() - runtime.freeMemory();
        var maxMemory = runtime.maxMemory();

        CloudAPI.instance().logger().info("Memory of node process&2: &3" + (currentMemory / mb) + "&2/&3" + (maxMemory / mb) + " mb");
    }
}
