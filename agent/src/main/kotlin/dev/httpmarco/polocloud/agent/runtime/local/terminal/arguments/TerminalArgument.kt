package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandContext

abstract class TerminalArgument<T>(open val key: String) {

    open fun defaultArgs(context: CommandContext): MutableList<String> {
        return mutableListOf()
    }

    // if one argument must be a special type
    open fun predication(rawInput: String): Boolean {
        return !(rawInput.startsWith("<") && rawInput.endsWith(">"))
    }

    open fun wrongReason(rawInput: String): String {
        return i18n.get("agent.terminal.command.reason.wrong")
    }

    abstract fun buildResult(input: String, context: CommandContext): T
}