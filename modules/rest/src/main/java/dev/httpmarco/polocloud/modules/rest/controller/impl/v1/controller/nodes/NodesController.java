package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.controller.nodes;

import com.google.gson.JsonObject;
import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request;
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.util.JsonUtils;
import io.javalin.http.Context;

public class NodesController extends Controller {

    public NodesController(RestModule restModule) {
        super("/nodes", restModule);
    }

    @Request(requestType = RequestType.GET, path = "/", permission = "polocloud.nodes.list")
    public void list(Context context) {
        var response = new JsonObject();
        var localNode = new JsonObject();
        var headNode = new JsonObject();

        var headNodeData = JsonUtils.GSON.toJsonTree(Node.instance().clusterProvider().headNode().data());
        var headNodeSituation = JsonUtils.GSON.toJsonTree(Node.instance().clusterProvider().headNode().situation());

        headNode.add("data", headNodeData);
        headNode.add("situation", headNodeSituation);

        var localNodeData = JsonUtils.GSON.toJsonTree(Node.instance().clusterProvider().localNode().data());
        var localNodeSituation = JsonUtils.GSON.toJsonTree(Node.instance().clusterProvider().localNode().situation());

        localNode.add("data", localNodeData);
        localNode.add("situation", localNodeSituation);

        response.add("headNode", headNode);
        response.add("localNode", localNode);

        Node.instance().clusterProvider().endpoints().forEach(it -> {
            String node = JsonUtils.GSON.toJson(it.data());
            response.addProperty("endpoints", node); //TODO
        });

        context.status(200).result(response.toString());
    }

}
