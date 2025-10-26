package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.service

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.service.ServiceCommandModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import dev.httpmarco.polocloud.shared.service.toJson
import io.javalin.http.Context

class ServiceController : Controller("/service") {

    @Request(requestType = RequestType.GET, path = "s/count", permission = "polocloud.service.count")
    fun serviceCount(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: 0L

        val services = polocloudShared.serviceProvider().findAll()
        val totalCount = services.count()

        if (from == 0L || to == 0L) {
            val data = JsonObject().apply {
                addProperty("serviceCount", totalCount)
            }

            context.defaultResponse(200, data = data)
            return
        }

        if (from < 0 || to < 0 || from > to) {
            context.defaultResponse(400, "Invalid range")
            return
        }

        val current = services.count { it.information.createdAt in from..to }
        val previous = services.count { it.information.createdAt < from }

        val percentage = when {
            previous > 0 -> (current * 100.0 / previous)
            current > 0 && previous == 0 -> current * 100.0
            else         -> 0.0
        }

        val data = JsonObject().apply {
            addProperty("serviceCount", current)
            addProperty("percentage", percentage)
        }

        context.defaultResponse(200, data = data)
    }

    @Request(requestType = RequestType.GET, path = "s/list", permission = "polocloud.service.list")
    fun listService(context: Context) {
        val services = polocloudShared.serviceProvider().findAll()
        context.defaultResponse(200, data = JsonArray().apply { services.map { service -> add(service.toJson()) } })
    }

    @Request(requestType = RequestType.POST, path = "/{serviceName}/command", permission = "polocloud.service.execute")
    fun command(context: Context) {
        val model = context.parseBodyOrBadRequest<ServiceCommandModel>() ?: return
        if (!context.validate(model.command.isNotBlank(), "Command is required")) return

        val serviceName = context.pathParam("serviceName")
        val service = polocloudShared.serviceProvider().find(serviceName)

        if (service == null) {
            context.defaultResponse(404,"Service not found")
            return
        }

        Agent.runtime.expender().executeCommand(service as AbstractService, model.command)
        context.defaultResponse(200,"Trying to execute command on service")
    }

    @Request(requestType = RequestType.PATCH, path = "/{serviceName}/restart", permission = "polocloud.service.restart")
    fun restart(context: Context) {
        val serviceName = context.pathParam("serviceName")
        val service = polocloudShared.serviceProvider().find(serviceName)

        if (service == null) {
            context.defaultResponse(404,"Service not found")
            return
        }

        service.shutdown()
        context.defaultResponse(202, "Service is restarting")
    }

}