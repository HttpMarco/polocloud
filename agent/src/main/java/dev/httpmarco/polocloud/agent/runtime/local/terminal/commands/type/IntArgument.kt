package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandContext

class IntArgument(key: String) : CommandArgument<Int>(key) {
    override fun predication(rawInput: String): Boolean {
        try {
            rawInput.toInt()
            return true
        } catch (_: NumberFormatException) {
            return false
        }
    }

    override fun wrongReason(): String {
        return i18n.get("agent.terminal.command.argument.type.int.wrong")
    }

    override fun defaultArgs(context: CommandContext): MutableList<String> {
        return mutableListOf()
    }

    override fun buildResult(input: String, context: CommandContext): Int {
        return input.toInt()
    }
}
