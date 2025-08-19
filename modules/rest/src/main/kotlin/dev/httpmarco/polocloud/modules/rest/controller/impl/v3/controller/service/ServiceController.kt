package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.service

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class ServiceController : Controller("/service") {

    @Request(requestType = RequestType.GET, path = "s/count", permission = "polocloud.service.count")
    fun serviceCount(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: 0L

        val services = polocloudShared.serviceProvider().findAll()
        val totalCount = services.count()

        if (from == 0L || to == 0L) {
            context.status(200).json(
                JsonObject().apply {
                    addProperty("serviceCount", totalCount)
                }.toString()
            )
            return
        }

        if (from < 0 || to < 0 || from > to) {
            context.status(400).json(message("Invalid range"))
            return
        }

        val current = services.count { it.information.createdAt in from..to }
        val previous = services.count { it.information.createdAt < from }

        val percentage = when {
            previous > 0 -> (current * 100.0 / previous)
            current > 0 && previous == 0 -> current * 100.0
            else         -> 0.0
        }

        context.status(200).json(
            JsonObject().apply {
                addProperty("serviceCount", current)
                addProperty("percentage", percentage)
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "s/list", permission = "polocloud.service.list")
    fun listService(context: Context) {
        val services = polocloudShared.serviceProvider().findAll()

        context.status(200).json(
            JsonArray().apply {
                services.map { service ->
                    add(
                        JsonObject().apply {
                            addProperty("id", service.id)
                            addProperty("name", service.name())
                            addProperty("state", service.state.name)
                            addProperty("type", service.type.name)
                            addProperty("groupName", service.groupName)
                            addProperty("hostname", service.hostname)
                            addProperty("port", service.port)
                            addProperty("templates", service.state.name)
                            add("information", JsonObject().apply {
                                addProperty("createdAt", service.information.createdAt)
                            })
                            add("templates", JsonArray().apply {
                                service.templates.forEach { template ->
                                    add(template)
                                }
                            })
                            add("properties", JsonObject().apply {
                                service.properties.forEach { (key, value) ->
                                    addProperty(key, value)
                                }
                            })
                            addProperty("minMemory", service.minMemory)
                            addProperty("maxMemory", service.maxMemory)
                            addProperty("playerCount", service.playerCount)
                            addProperty("maxPlayerCount", service.maxPlayerCount)
                            addProperty("memoryUsage", service.memoryUsage)
                            addProperty("cpuUsage", service.cpuUsage)
                            addProperty("motd", service.motd)
                        }
                    )
                }
            }.toString()
        )
    }
}