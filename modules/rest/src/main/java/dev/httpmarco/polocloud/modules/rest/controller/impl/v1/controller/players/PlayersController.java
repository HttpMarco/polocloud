package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.controller.players;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request;
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType;
import dev.httpmarco.polocloud.node.Node;
import io.javalin.http.Context;


public class PlayersController extends Controller {

    public PlayersController(RestModule restModule) {
        super("/players", restModule);
    }

    @Request(requestType = RequestType.GET, path = "/", permission = "polocloud.players.list")
    public void list(Context context) {
        var response = new JsonObject();
        var players = new JsonArray();

        Node.instance().playerProvider().players().forEach(clusterPlayer -> players.add(clusterPlayer.name()));
        response.add("players", players);

        context.status(200).result(response.toString());
    }
}
