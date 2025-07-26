package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext

class TextArgument(key: String) : TerminalArgument<String>(key) {

    override fun buildResult(input: String, context: InputContext): String {
        return input
    }
}
