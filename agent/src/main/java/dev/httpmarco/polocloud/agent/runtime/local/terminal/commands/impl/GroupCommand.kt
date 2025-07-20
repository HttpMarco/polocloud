package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.groups.GroupData
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeGroupStorage
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.GroupArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.GroupEditFlagArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.IntArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.KeywordArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.PlatformArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.PlatformVersionArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.TextArgument
import dev.httpmarco.polocloud.platforms.PlatformIndex

class GroupCommand(private val groupStorage: RuntimeGroupStorage) : Command("group", "Manage all group actions") {

    init {
        syntax(execution = {
            if (groupStorage.items().isEmpty()) {
                i18n.info("agent.terminal.command.group.not-found")
                return@syntax
            }
            i18n.info("agent.terminal.command.group.found", groupStorage.items().size)
            groupStorage.items().forEach { i18n.info("agent.terminal.command.group.list", it.data.name, it.data.minOnlineService, it.serviceCount()) }
        }, KeywordArgument("list"))

        val groupArgument = GroupArgument()

        syntax(execution = { context ->
            val group = context.arg(groupArgument)

            i18n.info("agent.terminal.command.group.info.header", group.data.name)
            i18n.info("agent.terminal.command.group.info.line.1", group.data.minMemory)
            i18n.info("agent.terminal.command.group.info.line.2", group.data.maxMemory)
            i18n.info("agent.terminal.command.group.info.line.3", group.data.minOnlineService)
            i18n.info("agent.terminal.command.group.info.line.4", group.data.maxOnlineService)
            i18n.info("agent.terminal.command.group.info.line.5", group.serviceCount())
            i18n.info("agent.terminal.command.group.info.line.6", group.data.platform.group, group.data.platform.version)
            i18n.info("agent.terminal.command.group.info.line.7")

            group.data.properties.forEach { (key, value) ->
                logger.info("   &8- &7$key&8: &f$value")
            }

        }, groupArgument)


        syntax(execution = { context ->
            val editType = context.arg(GroupEditFlagArgument())
            val group = context.arg(groupArgument)
            val value = context.arg(TextArgument("value"))

            when (editType) {
                GroupEditFlagArgument.TYPES.MIN_ONLINE_SERVICES -> {
                    val intValue = value.toInt()
                    if (intValue > group.data.maxOnlineService) {
                        i18n.warn("agent.terminal.command.group.create.warn.above-max", editType.name)
                        return@syntax
                    }
                    group.data.minOnlineService = intValue
                }
                GroupEditFlagArgument.TYPES.MAX_ONLINE_SERVICES -> {
                    val intValue = value.toInt()
                    if (intValue < group.data.minOnlineService) {
                        i18n.warn("agent.terminal.command.group.edit.warn.below-min", editType.name)
                        return@syntax
                    }
                    group.data.maxOnlineService = intValue
                }
                GroupEditFlagArgument.TYPES.MIN_MEMORY -> {
                    val intValue = value.toInt()
                    if (intValue > group.data.maxMemory) {
                        i18n.warn("agent.terminal.command.group.edit.warn.above-max", editType.name)
                        return@syntax
                    }
                    group.data.minMemory = intValue
                }
                GroupEditFlagArgument.TYPES.MAX_MEMORY -> {
                    val intValue = value.toInt()
                    if (intValue < group.data.minMemory) {
                        i18n.warn("agent.terminal.command.group.edit.warn.below-min", editType.name)
                        return@syntax
                    }
                    group.data.maxMemory = intValue
                }
            }

            group.update()
            i18n.info("agent.terminal.command.group.edit.successful", group.data.name, editType.name, value)
        }, groupArgument, KeywordArgument("edit"), GroupEditFlagArgument(), TextArgument("value"))

        syntax(execution = { context ->
            val group = context.arg(groupArgument)
            i18n.info("agent.terminal.command.group.shutdown-all")
            group.shutdownAll()
            i18n.info("agent.terminal.command.group.shutdown-all.successful", group.data.name)
        }, groupArgument, KeywordArgument("shutdownAll"))

        syntax(execution = { context ->
            val group = context.arg(groupArgument)

            Agent.instance.runtime.groupStorage().destroy(group)
            group.shutdownAll()

            i18n.info("agent.terminal.command.group.deleted", group.data.name)
        }, groupArgument, KeywordArgument("delete"))

        val nameArgument = TextArgument("name")
        val platformArgument = PlatformArgument()
        val platformVersionArgument = PlatformVersionArgument(platformArgument)
        val minOnlineServices = IntArgument("minOnlineServices")
        val maxOnlineServices = IntArgument("maxOnlineServices")
        val minMemory = IntArgument("minMemory")
        val maxMemory = IntArgument("maxMemory")

        syntax(
            execution = { context ->

                val minMemory = context.arg(minMemory)
                val maxMemory = context.arg(maxMemory)

                if (maxMemory < minMemory) {
                    i18n.warn("agent.terminal.command.group.create.invalid-memory-range")
                    return@syntax
                }

                val minOnlineServices = context.arg(minOnlineServices)
                val maxOnlineServices = context.arg(maxOnlineServices)

                if (maxOnlineServices < minOnlineServices) {
                    i18n.warn("agent.terminal.command.group.create.invalid-services-range")
                    return@syntax
                }

                val platform = context.arg(platformArgument)
                val groupName = context.arg(nameArgument)

                if (Agent.instance.runtime.groupStorage().items().any { it.data.name.equals(groupName, ignoreCase = true) }) {
                    i18n.warn("agent.terminal.command.group.create.already-exists", groupName)
                    return@syntax
                }

                Agent.instance.runtime.groupStorage().publish(
                    Group(
                        GroupData(
                            groupName,
                            PlatformIndex(
                                platform.name,
                                context.arg(platformVersionArgument).version
                            ),
                            minMemory,
                            maxMemory,
                            minOnlineServices,
                            maxOnlineServices,
                            listOf("EVERY", "EVERY_" + platform.type, groupName),
                            emptyMap()
                        )
                    )
                )
                i18n.info("agent.terminal.command.group.create.successful")
            },
            KeywordArgument("create"),
            nameArgument,
            platformArgument,
            platformVersionArgument,
            minMemory,
            maxMemory,
            minOnlineServices,
            maxOnlineServices
        )
    }
}