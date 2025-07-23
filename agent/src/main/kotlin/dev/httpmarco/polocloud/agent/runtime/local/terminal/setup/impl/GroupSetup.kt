package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.impl

import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.groups.GroupData
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.PlatformArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.TextArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.Setup
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.SetupStep
import dev.httpmarco.polocloud.platforms.PlatformIndex

class GroupSetup : Setup<Group>("Group setup") {

    private val nameArgument = TextArgument("name")
    private val platformArgument = PlatformArgument("platform")

    override fun bindQuestion() {
        attach(SetupStep("agent.local-runtime.setup.group.name", nameArgument))
        attach(SetupStep("agent.local-runtime.setup.group.platform", platformArgument))
    }

    override fun onComplete(result: InputContext): Group {


        return Group(GroupData(
            result.arg(nameArgument),
            PlatformIndex(result.arg(platformArgument).name, ""),
            0,
            0,
            0,
            0,
            emptyList(),
            emptyMap())
        )
    }
}