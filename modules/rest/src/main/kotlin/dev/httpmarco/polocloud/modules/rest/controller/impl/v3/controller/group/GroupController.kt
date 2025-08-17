package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.group

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group.GroupCountModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class GroupController : Controller("/group") {

    @Request(requestType = RequestType.GET, path = "s/count", permission = "polocloud.group.count")
    fun groupCount(context: Context) {
        val groupCountModel = try {
            context.bodyAsClass(GroupCountModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        val groups = polocloudShared.groupProvider().findAll()
        val totalCount = groups.count()

        if (groupCountModel.from == 0L || groupCountModel.to == 0L) {
            context.status(200).json(
                JsonObject().apply {
                    addProperty("groupCount", totalCount)
                }.toString()
            )
            return
        }

        if (groupCountModel.from < 0 || groupCountModel.to < 0 || groupCountModel.from > groupCountModel.to) {
            context.status(400).json(message("Invalid range"))
            return
        }

        val filteredGroups = groups.filter { it.information.createdAt in groupCountModel.from..groupCountModel.to }

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