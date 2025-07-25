package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument

class YesNotArgument(key: String) : TerminalArgument<Boolean>(key) {

    init {
        bindShortcut('y', "yes")
        bindShortcut('n', "no")
    }

    override fun buildResult(input: String, context: InputContext): Boolean {
        if (input.equals("yes", ignoreCase = true)) {
            return true
        } else if (input.equals("no", ignoreCase = true)) {
            return false
        }
        throw IllegalArgumentException("Invalid input for YesNotArgument: '$input'. Expected 'yes' or 'no'.")
    }

    override fun predication(rawInput: String): Boolean {
        return rawInput.equals("yes", ignoreCase = true) || rawInput.equals("no", ignoreCase = true)
    }
}