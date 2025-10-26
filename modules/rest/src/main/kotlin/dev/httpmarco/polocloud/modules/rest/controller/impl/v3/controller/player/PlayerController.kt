package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.player

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.player.toJson
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class PlayerController : Controller("/player") {

    @Request(requestType = RequestType.GET, path = "/{playerName}", permission = "polocloud.player.get")
    fun getPlayer(context: Context) {
        val playerName = context.pathParam("playerName")
        if (playerName.isEmpty()) {
            context.defaultResponse(400,"Player name cannot be null or empty")
            return
        }

        val player = polocloudShared.playerProvider().findByName(playerName)
        if (player == null) {
            context.defaultResponse(404,"Player not found")
            return
        }

        context.defaultResponse(200, data = player.toJson())
    }

    //TODO implement players right

    @Request(requestType = RequestType.GET, path = "s/list", permission = "polocloud.players.list")
    fun listPlayers(context: Context) {
        val page = context.queryParam("page")?.toIntOrNull() ?: 1
        val size = context.queryParam("size")?.toIntOrNull() ?: 20

        if (page < 1 || size < 1 || size > 100) {
            context.status(400).result("Invalid pagination parameters. Page must be >= 1, size must be 1-100")
            return
        }

        val allPlayers = polocloudShared.playerProvider().findAll()

        if (allPlayers.isEmpty()) {
            context.status(200).json(
                JsonObject().apply {
                    addProperty("page", page)
                    addProperty("size", size)
                    addProperty("total", 0)
                    addProperty("totalPages", 0)
                    add("data", JsonArray())
                }.toString()
            )
            return
        }

        val total = allPlayers.size
        val totalPages = (total + size - 1) / size
        val startIndex = (page - 1) * size
        val endIndex = minOf(startIndex + size, total)

        val paginatedPlayers = allPlayers.subList(startIndex, endIndex)

        context.status(200).json(
            JsonObject().apply {
                addProperty("page", page)
                addProperty("size", size)
                addProperty("total", total)
                addProperty("totalPages", totalPages)
                add("data", JsonArray().apply {
                    paginatedPlayers.forEach { player ->
                        add(
                            JsonObject().apply {
                                addProperty("name", player.name)
                                addProperty("uuid", player.uniqueId.toString())
                                addProperty("currentServiceName", player.currentServiceName)
                            }
                        )
                    }
                })
            }.toString()
        )
    }
}
