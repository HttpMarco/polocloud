package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext

class TextArgument(key: String) : TerminalArgument<String>(key) {

    override fun buildResult(input: String, context: InputContext): String {
        return input
    }

    override fun predication(rawInput: String): Boolean {
        return rawInput.isNotBlank()
    }

    override fun wrongReason(rawInput: String): String {
        return i18n.get("agent.terminal.setup.argument.text.empty")
    }
}
