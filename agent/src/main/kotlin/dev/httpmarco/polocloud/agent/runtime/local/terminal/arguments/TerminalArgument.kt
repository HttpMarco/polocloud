package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments

import dev.httpmarco.polocloud.agent.i18n

abstract class TerminalArgument<T>(open val key: String) {

    private val shortcuts = arrayListOf<TerminalShortCut>()

    open fun defaultArgs(context: InputContext): MutableList<String> {
        return mutableListOf()
    }

    // if one argument must be a special type
    open fun predication(rawInput: String): Boolean {
        return !(rawInput.startsWith("<") && rawInput.endsWith(">"))
    }

    open fun wrongReason(rawInput: String): String {
        return i18n.get("agent.terminal.command.reason.wrong")
    }

    abstract fun buildResult(input: String, context: InputContext): T

    fun bindShortcut(key: Char, value: String) {
        shortcuts.add(TerminalShortCut(key, value))
    }
}