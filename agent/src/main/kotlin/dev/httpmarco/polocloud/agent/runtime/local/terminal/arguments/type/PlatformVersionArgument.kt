package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandContext
import dev.httpmarco.polocloud.platforms.PlatformVersion

class PlatformVersionArgument(val platformArgument: PlatformArgument) : TerminalArgument<PlatformVersion>("version"){

    override fun buildResult(
        input: String,
        context: CommandContext
    ): PlatformVersion {
        return context.arg(platformArgument).versions.stream().filter { it.version == input }.findFirst().orElse(null)
    }

    override fun defaultArgs(context: CommandContext): MutableList<String> {
        return context.arg(platformArgument).versions.stream().map { it.version }.toList()
    }
}