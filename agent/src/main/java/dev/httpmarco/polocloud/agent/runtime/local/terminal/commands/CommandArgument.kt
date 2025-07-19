package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands

import dev.httpmarco.polocloud.agent.i18n

abstract class CommandArgument<T>(open val key: String) {

    open fun defaultArgs(context: CommandContext): MutableList<String> {
        return mutableListOf()
    }

    // if one argument must be a special type
    open fun predication(rawInput: String): Boolean {
        return !(rawInput.startsWith("<") && rawInput.endsWith(">"))
    }

    open fun wrongReason(): String {
        return i18n.get("agent.terminal.command.reason.wrong")
    }

    abstract fun buildResult(input: String, context: CommandContext): T
}