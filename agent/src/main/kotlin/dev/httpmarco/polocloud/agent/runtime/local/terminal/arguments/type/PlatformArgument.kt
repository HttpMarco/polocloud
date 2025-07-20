package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.platforms.Platform
import dev.httpmarco.polocloud.platforms.PlatformPool

class PlatformArgument(key: String = "platform") : TerminalArgument<Platform>(key) {

    override fun buildResult(input: String, context: InputContext): Platform {
        return PlatformPool.find(input)!!
    }

    override fun defaultArgs(context: InputContext): MutableList<String> {
        return PlatformPool.platforms().stream().map { it.name }.toList()
    }
}