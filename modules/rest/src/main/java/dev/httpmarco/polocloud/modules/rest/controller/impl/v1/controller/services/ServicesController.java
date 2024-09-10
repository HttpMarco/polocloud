package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.controller.services;

import com.google.gson.JsonObject;
import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request;
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.util.JsonUtils;
import io.javalin.http.Context;

public class ServicesController extends Controller {

    public ServicesController(RestModule restModule) {
        super("/services", restModule);
    }

    @Request(requestType = RequestType.GET, path = "/", permission = "polocloud.services.list")
    public void list(Context context) {
        var response = new JsonObject();

        Node.instance().serviceProvider().services().forEach(service -> {
            var services = new JsonObject();

            services.addProperty("orderedId", service.orderedId());
            services.addProperty("name", service.name());
            services.addProperty("id", service.id().toString());
            services.addProperty("group", service.group().name());
            services.addProperty("port", service.port());
            services.addProperty("hostname", service.hostname());
            services.addProperty("runningNode", service.runningNode());
            services.addProperty("state", service.state().name());
            services.addProperty("maxPlayers", service.maxPlayers());
            services.add("onlinePlayers", JsonUtils.GSON.toJsonTree(service.onlinePlayers()));
            services.add("properties", JsonUtils.GSON.toJsonTree(service.properties()));

            response.add(service.name(), services);
        });
        context.status(200).result(response.toString());
    }
}
