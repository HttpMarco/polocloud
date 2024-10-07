package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.controller.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;
import dev.httpmarco.polocloud.modules.rest.controller.impl.v1.model.services.StartServiceModel;
import dev.httpmarco.polocloud.modules.rest.controller.impl.v1.model.services.StopServiceModel;
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request;
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.util.JsonUtils;
import io.javalin.http.Context;

import java.util.concurrent.CompletableFuture;

public class ServicesController extends Controller {

    public ServicesController(RestModule restModule) {
        super("/service", restModule);
    }

    @Request(requestType = RequestType.GET, path = "s/", permission = "polocloud.services.list")
    public void list(Context context) {
        var services = new JsonArray();

        Node.instance().serviceProvider().services().forEach(service -> {
            var serviceJson = new JsonObject();

            serviceJson.addProperty("orderedId", service.orderedId());
            serviceJson.addProperty("name", service.name());
            serviceJson.addProperty("id", service.id().toString());

            services.add(serviceJson);
        });

        context.status(200).json(services.toString());
    }

    @Request(requestType = RequestType.GET, path = "/{serviceName}", permission = "polocloud.service.view")
    public void getService(Context context) {
        var response = new JsonObject();
        var serviceName = context.pathParam("serviceName");

        var service = Node.instance().serviceProvider().find(serviceName);
        if (service == null) {
            context.status(404).json(message("Service not found"));
            return;
        }

        response.addProperty("orderedId", service.orderedId());
        response.addProperty("name", service.name());
        response.addProperty("id", service.id().toString());
        response.addProperty("group", service.group().name());

        response.addProperty("port", service.port());
        response.addProperty("hostname", service.hostname());
        response.addProperty("runningNode", service.runningNode());
        response.addProperty("state", service.state().name());
        response.addProperty("maxPlayers", service.maxPlayers());

        response.add("onlinePlayers", JsonUtils.GSON.toJsonTree(service.onlinePlayers()));
        response.add("properties", JsonUtils.GSON.toJsonTree(service.properties()));

        context.status(200).json(response.toString());
    }

    @Request(requestType = RequestType.POST, path = "/start", permission = "polocloud.service.start")
    public void startService(Context context) {
        StartServiceModel request;
        try {
            request = context.bodyAsClass(StartServiceModel.class);
        } catch (Exception e) {
            context.status(400).json(message("Invalid request body"));
            return;
        }

        if (request.group() == null || request.group().isEmpty()) {
            context.status(400).json(message("Group cannot be empty"));
            return;
        }

        if (request.amount() <= 0) {
            context.status(400).json(message("Amount must be higher than 0"));
            return;
        }

        var group = Node.instance().groupProvider().find(request.group());
        if (group == null) {
            context.status(404).json(message("Group not found"));
            return;
        }

        // Service starten
        CompletableFuture.runAsync(() -> start(group, request.amount()));

        var response = new JsonObject();
        response.addProperty("message", "Service is starting");

        context.status(202).json(response.toString());
    }

    @Request(requestType = RequestType.POST, path = "/{serviceName}/stop", permission = "polocloud.service.stop")
    public void stopService(Context context) {
        StopServiceModel request;
        try {
            request = context.bodyAsClass(StopServiceModel.class);
        } catch (Exception e) {
            context.status(400).json(message("Invalid request body"));
            return;
        }

        if (request.serviceName() == null || request.serviceName().isEmpty()) {
            context.status(400).json(message("Service name cannot be empty"));
            return;
        }

        var service = Node.instance().serviceProvider().find(request.serviceName());
        if (service == null) {
            context.status(404).json(message("Service not found"));
            return;
        }

        CompletableFuture.runAsync(service::shutdown);

        var response = new JsonObject();
        response.addProperty("message", "Service is stopping");

        context.status(202).json(response.toString());
    }

    //TODO add service update endpoint

    private void start(ClusterGroup group, int amount) {
        for (int i = 0; i < amount; i++) {
            Node.instance().serviceProvider().factory().runGroupService(group);
        }
    }

}
