package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.controller.groups;

import dev.httpmarco.polocloud.api.packet.resources.group.GroupCreatePacket;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;
import dev.httpmarco.polocloud.modules.rest.controller.impl.v1.model.groups.CreateGroupModel;
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request;
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.util.JsonUtils;
import io.javalin.http.Context;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class GroupsController extends Controller {

    public GroupsController(RestModule restModule) {
        super("/group", restModule);
    }

    @Request(requestType = RequestType.GET, path = "s/", permission = "polocloud.groups.list")
    public void list(Context context) {
        var groups = Node.instance().groupProvider().groups();
        context.status(200).result(JsonUtils.GSON.toJson(groups));
    }

    @Request(requestType = RequestType.GET, path = "/{groupName}", permission = "polocloud.group.view")
    public void getGroup(Context context) {
        var groupName = context.pathParam("groupName");

        var groupOptional = Optional.ofNullable(Node.instance().groupProvider().find(groupName));
        if (groupOptional.isEmpty()) {
            context.status(404).result(failMessage("Group not found"));
            return;
        }

        context.status(200).result(JsonUtils.GSON.toJson(groupOptional.get()));
    }

    @Request(requestType = RequestType.POST, path = "/", permission = "polocloud.group.create")
    public void createGroup(Context context) {
        CreateGroupModel request;
        try {
            request = context.bodyAsClass(CreateGroupModel.class);
        } catch (Exception e) {
            context.status(400).result(failMessage("Invalid body"));
            return;
        }

        if (request.name() == null || request.name().isEmpty()) {
            context.status(400).result("Name cannot be empty");
            return;
        }

        var platform = Node.instance().platformService().find(request.platform());
        if (platform == null) {
            context.status(400).result("Invalid platform ID");
            return;
        }

        var platformVersion = platform.versions().stream()
                .filter(it -> it.version().equalsIgnoreCase(request.version()))
                .findFirst();
        if (platformVersion.isEmpty()) {
            context.status(400).result("Invalid platform version");
            return;
        }

        if (request.maxMemory() < 512) {
            context.status(400).result("Max memory must be at least 512MB");
            return;
        }

        var version = platformVersion.get();
        var name = request.name();

        context.status(200);
        CompletableFuture.runAsync(() -> Node.instance().clusterProvider().broadcastAll(new GroupCreatePacket(name,
                new String[]{"every", platform.type().defaultTemplateSpace(), name},
                new String[]{Node.instance().clusterProvider().localNode().data().name()},
                new PlatformGroupDisplay(platform.id(), version.version(), platform.type()),
                request.maxMemory(),
                request.staticService(),
                request.minOnlineServices(),
                request.maxOnlineServices(),
                request.fallback())));
    }

    @Request(requestType = RequestType.DELETE, path = "/{groupName}", permission = "polocloud.group .delete")
    public void deleteGroup(Context context) {
        var groupName = context.pathParam("groupName");
        var group = Node.instance().groupProvider().find(groupName);

        if (group == null) {
            context.status(404).result(failMessage("Group not found"));
            return;
        }

        context.status(200);
        CompletableFuture.runAsync(() -> Node.instance().groupProvider().delete(group.name()));
    }


}
