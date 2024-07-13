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
import dev.httpmarco.polocloud.api.node.Node;
import dev.httpmarco.polocloud.api.node.NodeService;
import dev.httpmarco.polocloud.api.packets.nodes.NodeBindPacket;
import dev.httpmarco.polocloud.base.CloudBase;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class CloudNodeService implements NodeService {

    private final CloudNodeServiceFactory factory = new CloudNodeServiceFactory();

    private final LocalNode localNode;
    private final Set<ExternalNode> externalNodes;

    @Setter
    private Node headNode;

    public CloudNodeService(LocalNode localNode, Set<ExternalNode> externalNodes) {
        this.externalNodes = externalNodes;
        this.localNode = localNode;

        localNode.server().responder("node-verify", property -> {

            CloudAPI.instance().logger().warn("An external node try to connect...");

            // we check the information are correct
            var selfId = property.getUUID("self-id");
            var selfName = property.getString("self-name");

            var externalNodeName = property.getString("name");
            var externalNodeId = property.getUUID("id");
            var externalHostname = property.getString("hostname");
            var externalPort = property.getInteger("port");

            if (!(selfId.equals(localNode.id()) && selfName.equals(localNode.name()))) {
                CloudAPI.instance().logger().warn("External was blocked, because the given data is not correct!");
                return new NodeBindPacket(false, "Given data is not correct.", null);
            }

            if (externalNodes.stream().anyMatch(it -> it.name().equals(externalNodeName))) {
                CloudAPI.instance().logger().warn("External was blocked, because a duplicate node name.");
                return new NodeBindPacket(false, "The node name is already exist.", null);
            }

            if (externalNodes.stream().anyMatch(it -> it.id().equals(externalNodeId))) {
                CloudAPI.instance().logger().warn("External was blocked, because a duplicate node id.");
                return new NodeBindPacket(false, "The node id is already exist.", null);
            }
            // correct external node
            var externalNode = new ExternalNode(externalNodeId, externalNodeName, externalHostname, externalPort);

            this.externalNodes.add(externalNode);
            // save configuration
            CloudBase.instance().cloudConfiguration().content().externalNodes().add(externalNode);
            CloudBase.instance().cloudConfiguration().save();

            CloudAPI.instance().logger().success("External node was successfully connected&2. (&1" + externalNode + "&2)");
            return new NodeBindPacket(true, null, localNode);
        });
    }

    @Contract("_, _, _, _ -> new")
    @Override
    public @NotNull Node generateNode(String hostname, int port, UUID uuid, String name) {
        return new ExternalNode(uuid, name, hostname, port);
    }
}
