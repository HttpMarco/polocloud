package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.service

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.service.ServiceCountModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class ServiceController : Controller("/service") {

    @Request(requestType = RequestType.GET, path = "s/count", permission = "polocloud.service.count")
    fun serviceCount(context: Context) {
        val serviceCountModel = try {
            context.bodyAsClass(ServiceCountModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        val services = polocloudShared.serviceProvider().findAll()
        val totalCount = services.count()

        if (serviceCountModel.from == 0L || serviceCountModel.to == 0L) {
            context.status(200).json(
                JsonObject().apply {
                    addProperty("serviceCount", totalCount)
                }.toString()
            )
            return
        }

        if (serviceCountModel.from < 0 || serviceCountModel.to < 0 || serviceCountModel.from > serviceCountModel.to) {
            context.status(400).json(message("Invalid range"))
            return
        }

        val filteredServices = services.filter { it.information.createdAt in serviceCountModel.from..serviceCountModel.to }

        val filteredCount = filteredServices.count()
        val percentage = if (totalCount > 0) (filteredCount.toDouble() / totalCount * 100).toInt() else 0

        context.status(200).json(
            JsonObject().apply {
                addProperty("serviceCount", filteredCount)
                addProperty("percentage", percentage)
            }.toString()
        )
    }
}