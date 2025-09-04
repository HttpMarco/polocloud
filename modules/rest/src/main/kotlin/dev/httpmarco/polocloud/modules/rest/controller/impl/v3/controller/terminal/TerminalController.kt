package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.terminal

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.terminal.TerminalCommandModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context

class TerminalController : Controller("/terminal") {

    @Request(requestType = RequestType.POST, path = "/command", permission = "polocloud.terminal.command")
    fun command(context: Context) {
        val model = context.parseBodyOrBadRequest<TerminalCommandModel>() ?: return
        if (!context.validate(model.command.isNotBlank(), "Command is required")) return

        Agent.runtime.sendCommand(model.command)
        context.defaultResponse(201, "Trying to execute command")
    }
}