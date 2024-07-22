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

import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;

@Command(command = "info", aliases = {"me"})
public final class InfoCommand {

    @DefaultCommand
    public void handle() {

        var thisNode = Node.instance().nodeProvider().localEndpoint().data();

        Node.instance().logger().info("Self node Id&8: &b" + thisNode.id());
        Node.instance().logger().info("Registered groups&8: &b" + Node.instance().groupProvider().groups().size());
        Node.instance().logger().info("Online services&8: &b" + Node.instance().serviceProvider().services().size());

        var runtime = Runtime.getRuntime();
        int mb = 1024 * 1024;
        var currentMemory = runtime.totalMemory() - runtime.freeMemory();
        var maxMemory = runtime.maxMemory();

        Node.instance().logger().info("Memory of node process&8: &b" + (currentMemory / mb) + "&8/&b" + (maxMemory / mb) + " mb");
    }
}
