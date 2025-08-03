package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext

class GroupArgument(key: String = "group") : TerminalArgument<AbstractGroup>(key) {

    override fun buildResult(input: String, context: InputContext): AbstractGroup {
        return Agent.runtime.groupStorage().find(input)!!
    }

    override fun defaultArgs(context: InputContext): MutableList<String> {
        return Agent.runtime.groupStorage().findAll().map { it.name }.toMutableList()
    }

    override fun predication(rawInput: String): Boolean {
        return Agent.runtime.groupStorage().find(rawInput) != null
    }
}