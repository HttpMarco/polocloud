package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument

class SetupStep<T>(val questionKey: String, val argument: TerminalArgument<T>, val action: (T) -> Unit = {}) {

}