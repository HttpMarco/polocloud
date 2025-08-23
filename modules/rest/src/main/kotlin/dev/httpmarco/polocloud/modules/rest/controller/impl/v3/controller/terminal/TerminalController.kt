package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.terminal

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.terminal.TerminalCommandModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context

class TerminalController : Controller("/terminal") {

    @Request(requestType = RequestType.POST, path = "/command", permission = "polocloud.terminal.command")
    fun command(context: Context) {
        val terminalCommandModel = try {
            context.bodyAsClass(TerminalCommandModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (terminalCommandModel.command.isBlank()) {
            context.status(400).json(message("Invalid body: command cannot be empty"))
            return
        }

        Agent.runtime.sendCommand(terminalCommandModel.command)
        context.status(200).json(message("Trying to execute command"))
    }
}