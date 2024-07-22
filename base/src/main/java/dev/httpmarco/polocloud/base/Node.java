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

package dev.httpmarco.polocloud.base;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.server.CommunicationServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.player.CloudPlayerProvider;
import dev.httpmarco.polocloud.api.properties.PropertyPool;
import dev.httpmarco.polocloud.base.common.NodeHeader;
import dev.httpmarco.polocloud.base.events.GlobalEventNode;
import dev.httpmarco.polocloud.base.groups.CloudGroupProviderImpl;
import dev.httpmarco.polocloud.base.logging.FileLoggerHandler;
import dev.httpmarco.polocloud.base.logging.Logger;
import dev.httpmarco.polocloud.base.node.NodeHeadCalculator;
import dev.httpmarco.polocloud.base.node.NodeSituation;
import dev.httpmarco.polocloud.base.node.packets.ClusterHeadNodeResponse;
import dev.httpmarco.polocloud.base.node.tasks.ClusterBindTask;
import dev.httpmarco.polocloud.base.node.NodeProvider;
import dev.httpmarco.polocloud.base.player.CloudPlayerProviderImpl;
import dev.httpmarco.polocloud.base.services.CloudServiceProviderImpl;
import dev.httpmarco.polocloud.base.templates.TemplatesService;
import dev.httpmarco.polocloud.base.terminal.CloudTerminal;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class Node extends CloudAPI {

    private final Logger logger = new Logger();
    private final NodeModel nodeModel = NodeModel.read();

    private final CloudTerminal terminal = new CloudTerminal();
    private final NodeProvider nodeProvider = new NodeProvider();

    private final GlobalEventNode globalEventNode = new GlobalEventNode();
    private final CloudGroupProviderImpl groupProvider = new CloudGroupProviderImpl();
    private final CloudServiceProviderImpl serviceProvider = new CloudServiceProviderImpl();
    private final TemplatesService templatesService = new TemplatesService();
    private final CloudPlayerProvider playerProvider = new CloudPlayerProviderImpl();

    boolean running = true;

    public Node() {
        // register logging layers (for general output)
        logger.factory().registerLoggers(new FileLoggerHandler(), terminal);
        NodeHeader.print(this.terminal);

        ClusterBindTask.bindCluster(this.nodeProvider).whenComplete((unused, throwable) -> {
            if (NodeHeadCalculator.isSelfHead()) {
                nodeProvider.localEndpoint().situation(NodeSituation.REACHABLE);
                nodeProvider.headNodeEndpoint(nodeProvider.localEndpoint());

                startProcess();
                return;
            }
            var externalNode = nodeProvider.externalNodeEndpoints().stream().filter(externalNodeEndpoint -> externalNodeEndpoint.situation() == NodeSituation.REACHABLE).findFirst().orElse(null);
            externalNode.transmit().request("cluster-head-node-request", ClusterHeadNodeResponse.class, packet -> {
                this.nodeProvider.headNodeEndpoint(this.nodeProvider.node(packet.node()));
                // todo sync all data
                this.nodeProvider.localEndpoint().situation(NodeSituation.REACHABLE);
                startProcess();
            });

        });
    }

    private void startProcess() {
        logger().success("Successfully started up&2! (&1Took " + (System.currentTimeMillis() - Long.parseLong(System.getProperty("startup"))) + "ms&2)");

        this.terminal.start();
        this.serviceProvider.queue().start();
    }

    @Override
    public PropertyPool globalProperties() {
        return nodeModel.properties();
    }

    public CommunicationServer transmitter() {
        return this.nodeProvider.localEndpoint().server();
    }

    public static Node instance() {
        return (Node) CloudAPI.instance();
    }

}