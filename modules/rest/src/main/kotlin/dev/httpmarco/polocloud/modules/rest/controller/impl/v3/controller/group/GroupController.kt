package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.group

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class GroupController : Controller("/group") {

    @Request(requestType = RequestType.GET, path = "s/count", permission = "polocloud.group.count")
    fun groupCount(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: 0L

        val groups = polocloudShared.groupProvider().findAll()
        val totalCount = groups.count()

        if (from == 0L || to == 0L) {
            context.status(200).json(
                JsonObject().apply {
                    addProperty("groupCount", totalCount)
                }.toString()
            )
            return
        }

        if (from < 0 || to < 0 || from > to) {
            context.status(400).json(message("Invalid range"))
            return
        }

        val filteredGroups = groups.filter { it.information.createdAt in from..to }

        val filteredCount = filteredGroups.count()
        val percentage = if (totalCount > 0) (filteredCount.toDouble() / totalCount * 100).toInt() else 0

        context.status(200).json(
            JsonObject().apply {
                addProperty("groupCount", filteredCount)
                addProperty("percentage", percentage)
            }.toString()
        )
    }
}