package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.endpoint.groups;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request;
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.util.JsonUtils;
import io.javalin.http.Context;

public class GroupsController extends Controller {

    public GroupsController(RestModule restModule) {
        super("/groups", restModule);
    }

    @Request(requestType = RequestType.GET, path = "/", permission = "polocloud.groups.list")
    public void list(Context context) {
        var groups = Node.instance().groupProvider().groups();
        context.status(200).result(JsonUtils.GSON.toJson(groups));
    }
}
