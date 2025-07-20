package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandContext

class StringArrayArgument(key: String) : TerminalArgument<String>(key) {

    override fun buildResult(input: String, context: CommandContext): String {
        return input
    }
}