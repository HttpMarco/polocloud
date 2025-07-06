package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandContext
import dev.httpmarco.polocloud.platforms.Platform
import dev.httpmarco.polocloud.platforms.PlatformPool

class PlatformArgument(key: String = "platform") : CommandArgument<Platform>(key) {

    override fun buildResult(input: String, context: CommandContext): Platform {
        return PlatformPool.find(input)!!
    }

    override fun defaultArgs(context: CommandContext): MutableList<String> {
        return PlatformPool.platforms().stream().map { it.name }.toList()
    }
}