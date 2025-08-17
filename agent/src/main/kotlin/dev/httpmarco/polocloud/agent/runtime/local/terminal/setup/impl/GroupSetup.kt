package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.impl

import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.*
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.Setup
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.SetupStep
import dev.httpmarco.polocloud.shared.groups.GroupInformation
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import dev.httpmarco.polocloud.v1.GroupType

class GroupSetup : Setup<AbstractGroup>("Group setup") {

    private val nameArgument = TextArgument("name")
    private val platformArgument = PlatformArgument("platform")
    private val platformVersionArgument = PlatformVersionArgument(platformArgument)
    private val minMemoryArgument = IntArgument("minMemory", 1)
    private val maxMemoryArgument = IntArgument("maxMemory", 1)
    private val percentageToStartNewService = IntArgument("percentageToStartNewService", 0)
    private val minOnlineServicesArgument = IntArgument("minOnlineServices", 0)
    private val maxOnlineServicesArgument = IntArgument("maxOnlineServices", -1)
    private val fallbackArgument = YesNotArgument("fallback")
    private val staticArgument = YesNotArgument("static")

    override fun bindQuestion() {
        attach(SetupStep("agent.local-runtime.setup.group.name", nameArgument))
        attach(SetupStep("agent.local-runtime.setup.group.platform", platformArgument) { platform ->
            if (platform.type == GroupType.SERVER) {
                attach(SetupStep("agent.local-runtime.setup.group.fallback", fallbackArgument))
            }
        })
        attach(SetupStep("agent.local-runtime.setup.group.platform.version", platformVersionArgument))
        attach(SetupStep("agent.local-runtime.setup.group.min-memory", minMemoryArgument))
        attach(SetupStep("agent.local-runtime.setup.group.max-memory", maxMemoryArgument))
        attach(SetupStep("agent.local-runtime.setup.group.percentageToStartNewService", percentageToStartNewService))
        attach(SetupStep("agent.local-runtime.setup.group.min-online-services", minOnlineServicesArgument))
        attach(SetupStep("agent.local-runtime.setup.group.max-online-services", maxOnlineServicesArgument))
        attach(SetupStep("agent.local-runtime.setup.group.static", staticArgument))
    }

    override fun onComplete(result: InputContext): AbstractGroup {
        val name = result.arg(nameArgument)
        val originalPlatform = result.arg(platformArgument)
        val platform = PlatformIndex(originalPlatform.name, result.arg(platformVersionArgument).version)
        val minMemory = result.arg(minMemoryArgument)
        val maxMemory = result.arg(maxMemoryArgument)
        val percentageToStartNewService = result.arg(percentageToStartNewService)
        val minOnlineServices = result.arg(minOnlineServicesArgument)
        val maxOnlineServices = result.arg(maxOnlineServicesArgument)
        val fallback = if (result.contains(fallbackArgument)) result.arg(fallbackArgument) else false
        val static = if (result.contains(staticArgument)) result.arg(staticArgument) else false

        val properties = HashMap<String, JsonPrimitive>()

        if (fallback) {
            properties.put("fallback", JsonPrimitive(true))
        }

        if (static) {
            properties.put("static", JsonPrimitive(true))
        }


        val group = AbstractGroup(
            name,
            minMemory,
            maxMemory,
            minOnlineServices,
            maxOnlineServices,
            percentageToStartNewService.toDouble(),
            platform,
            GroupInformation(System.currentTimeMillis()),
            listOf(
                "EVERY", "EVERY_" + originalPlatform.type.name, name
            ),
            properties
        )

        Agent.runtime.groupStorage().publish(group)
        return group
    }
}