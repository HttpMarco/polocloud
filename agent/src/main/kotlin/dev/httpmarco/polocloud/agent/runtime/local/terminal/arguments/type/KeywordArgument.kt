package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandContext

class KeywordArgument(key: String) : TerminalArgument<String>(key) {
    override fun defaultArgs(context: CommandContext): MutableList<String> {
        return listOf(key) as MutableList<String>
    }

    override fun wrongReason(rawInput: String): String {
        return ""
    }

    override fun buildResult(input: String, context: CommandContext): String {
        return ""
    }

    override fun predication(rawInput: String): Boolean {
        return super.predication(rawInput) && rawInput.equals(key, ignoreCase = true)
    }
}