package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext

fun interface CommandExecution {
    fun execute(inputContext: InputContext)
}