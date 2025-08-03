package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.shared.service.Service

class ServiceArgument(key: String = "service") : TerminalArgument<AbstractService>(key) {

    override fun buildResult(input: String, context: InputContext): AbstractService {
        // null check is done in the predication method
        return Agent.runtime.serviceStorage().find(input)!!
    }

    override fun defaultArgs(context: InputContext): MutableList<String> {
        return Agent.runtime.serviceStorage().findAll().stream().map { it.name() }.toList()
    }

    override fun predication(rawInput: String): Boolean {
        return Agent.runtime.serviceStorage().find(rawInput) != null
    }
}