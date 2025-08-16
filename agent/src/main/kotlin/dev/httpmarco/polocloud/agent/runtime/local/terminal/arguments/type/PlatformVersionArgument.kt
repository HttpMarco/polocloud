package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.platforms.PlatformPool
import dev.httpmarco.polocloud.platforms.PlatformVersion

class PlatformVersionArgument(val platformArgument: PlatformArgument) : TerminalArgument<PlatformVersion>("version"){

    override fun buildResult(
        input: String,
        context: InputContext
    ): PlatformVersion {
        return context.arg(platformArgument).versions.stream().filter { it.version == input }.findFirst().orElse(null)
    }

    override fun wrongReason(rawInput: String): String {
        return i18n.get("agent.terminal.setup.argument.platform.version.wrong")
    }

    override fun defaultArgs(context: InputContext): MutableList<String> {
        return context.arg(platformArgument).versions.stream().map { it.version }.toList()
    }

    override fun predication(rawInput: String): Boolean {
        return PlatformPool.platforms().firstOrNull { it -> it.versions.firstOrNull { it.version == rawInput } != null } != null
    }
}