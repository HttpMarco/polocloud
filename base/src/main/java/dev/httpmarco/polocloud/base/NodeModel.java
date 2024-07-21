package dev.httpmarco.polocloud.base;

import dev.httpmarco.polocloud.api.cluster.NodeData;
import dev.httpmarco.polocloud.api.properties.PropertyPool;
import dev.httpmarco.polocloud.base.common.PropertiesPoolSerializer;
import dev.httpmarco.polocloud.base.node.data.ClusterData;
import dev.httpmarco.pololcoud.common.StringUtils;
import dev.httpmarco.pololcoud.common.document.Document;

import java.nio.file.Path;
import java.util.HashSet;

public record NodeModel(NodeData localNode, ClusterData cluster, PropertyPool properties) {

    private static final NodeModel DEFAULT = new NodeModel(new NodeData("node-1", "127.0.0.1", 9090), new ClusterData("default-cluster", StringUtils.randomString(16), new HashSet<>()), new PropertyPool());
    private static final Document<NodeModel> document = new Document<>(Path.of("config.json"), DEFAULT, PropertiesPoolSerializer.ADAPTER);

    public void save() {
        document.save();
    }

    public static NodeModel read() {
        return document.value();
    }
}
