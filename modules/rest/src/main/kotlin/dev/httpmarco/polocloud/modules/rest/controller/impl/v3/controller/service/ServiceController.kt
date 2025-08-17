package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.service

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

        val rangeLength = to - from
        val previous = services.count { it.information.createdAt in (from - rangeLength)..from }

        val percentage = when {
            previous > 0 -> ((current - previous) * 100.0 / previous)
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
}