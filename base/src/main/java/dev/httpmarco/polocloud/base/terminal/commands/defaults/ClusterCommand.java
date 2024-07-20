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
import dev.httpmarco.polocloud.base.node.NodeSituation;
import dev.httpmarco.polocloud.base.node.tasks.ClusterDataSyncTask;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;
import lombok.SneakyThrows;

@Command(command = "cluster", aliases = {"nodes"}, description = "Manage all your nodes or connect other ones.")
public final class ClusterCommand {

    @DefaultCommand
    public void handle() {
        var logger = Node.instance().logger();
        logger.info("cluster merge <id> <hostname> <port> <token> - Merge your node in an existing cluster.");
    }

    @SubCommand(args = {"merge", "<id>", "<hostname>", "<port>", "<token>"})
    public void add(String id, String hostname, int port, String token) {
        /*
        var testingResult = ClusterBindTask.merge(id, hostname, port, token);

        testingResult.whenComplete((externalNodeEndpoint, throwable) -> {
            if (externalNodeEndpoint.situation() == NodeSituation.INITIALIZE || externalNodeEndpoint.situation() == NodeSituation.SYNC) {
                Node.instance().logger().warn("Cannot connect to the cluster, because the cluster endpoint is in the " + externalNodeEndpoint.situation() + " phase.");
                return;
            }

            if (externalNodeEndpoint.situation() == NodeSituation.NOT_AVAILABLE) {
                Node.instance().logger().warn("Cannot connect to the cluster, because the cluster endpoint is offline.");
                return;
            }

            ClusterDataSyncTask.run(externalNodeEndpoint, token).whenComplete((result, throwable1) -> {

                if (!result) {
                    Node.instance().logger().warn("The given data is not correct with the given endpoint!");
                    return;
                }

                var nodeModel = Node.instance().nodeModel();
                nodeModel.cluster().endpoints().add(externalNodeEndpoint.data());
                nodeModel.save();

                Node.instance().logger().success("Synced all endpoint data!");
            });
        });

         */
    }
}