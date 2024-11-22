package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.controller.platforms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request;
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType;
import dev.httpmarco.polocloud.node.Node;
import io.javalin.http.Context;

public class PlatformsController extends Controller {

    public PlatformsController(RestModule restModule) {
        super("/platform", restModule);
    }

    @Request(requestType = RequestType.GET, path = "s/", permission = "polocloud.platforms.list")
    public void list(Context context) {
        var response = new JsonObject();
        var platforms = new JsonArray();

        Node.instance().platformService().platforms().forEach(platform -> {
            var platformObject = new JsonObject();
            platformObject.addProperty("id", platform.id());
            platformObject.addProperty("type", platform.type().toString());

            platforms.add(platformObject);
        });
        response.add("platforms", platforms);

        context.status(200).json(response.toString());
    }

    @Request(requestType = RequestType.GET, path = "/{platform}", permission = "polocloud.platform.view")
    public void getPlatform(Context context) {
        var platformId = context.pathParam("platform");

        var platform = Node.instance().platformService().find(platformId);
        if (platform == null) {
            context.status(404).json(message("Platform not found"));
            return;
        }

        var response = new JsonObject();
        var versions = new JsonArray();

        platform.versions().forEach(platformVersions -> versions.add(platformVersions.version()));

        response.addProperty("id", platform.id());
        response.add("versions", versions);

        context.status(200).json(response.toString());
    }
}
