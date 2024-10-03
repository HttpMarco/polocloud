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

        response.addProperty("count", Node.instance().playerProvider().playersCount());

        Node.instance().playerProvider().players().forEach(clusterPlayer -> players.add(clusterPlayer.name()));
        response.add("players", players);

        context.status(200).json(response.toString());
    }

    @Request(requestType = RequestType.GET, path = "/{playerName}", permission = "polocloud.player.view")
    public void getPlayer(Context context) {
        var response = new JsonObject();
        var playerName = context.pathParam("playerName");

        var player = Node.instance().playerProvider().find(playerName);
        if (player == null) {
            context.status(404).json(message("Player not found"));
            return;
        }

        response.addProperty("name", player.name());
        response.addProperty("uuid", player.uniqueId().toString());
        response.addProperty("currentProxyName", player.currentProxyName());
        response.addProperty("currentServerName", player.currentServerName());

        context.status(200).json(response.toString());
    }
}
