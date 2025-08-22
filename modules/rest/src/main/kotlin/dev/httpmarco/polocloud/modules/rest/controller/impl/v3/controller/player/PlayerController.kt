package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.player

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class PlayerController : Controller("/player") {

    @Request(requestType = RequestType.GET, path = "/{playerName}", permission = "polocloud.player.get")
    fun getPlayer(context: Context) {
        val playerName = context.queryParam("playerName")

        if (playerName.isNullOrEmpty()) {
            context.status(400).result("Player name cannot be null or empty")
            return
        }

        val player = polocloudShared.playerProvider().findByName("playerName")
        if (player == null) {
            context.status(404).result("Player not found")
            return
        }

        context.status(200).json(
            JsonObject().apply {
                addProperty("name", player.name)
                addProperty("uuid", player.uniqueId.toString())
                addProperty("currentServiceName", player.currentServiceName)
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "s/list", permission = "polocloud.players.list")
    fun listPlayers(context: Context) {
        val players = polocloudShared.playerProvider().findAll()//TODO pagination

        if (players.isEmpty()) {
            context.status(404).result("No players found")
            return
        }

        context.status(200).json(
            JsonArray().apply {
                players.forEach { player -> //TODO pagination
                    add(
                        JsonObject().apply {
                            addProperty("name", player.name)
                            addProperty("uuid", player.uniqueId.toString())
                            addProperty("currentServiceName", player.currentServiceName)
                        }
                    )
                }
            }.toString()
        )
    }

}