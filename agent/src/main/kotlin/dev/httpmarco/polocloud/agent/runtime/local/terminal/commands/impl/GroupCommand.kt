package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.groups.GroupData
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeGroupStorage
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.GroupArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.GroupEditFlagArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.IntArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.KeywordArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.PlatformArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.PlatformVersionArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.TextArgument
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
            var editType = context.arg(GroupEditFlagArgument())
            var group = context.arg(groupArgument)

            var stringValue = context.arg(TextArgument("value"))

            fun convertValueToInt(argument: IntArgument): Int? {
                if (!argument.predication(stringValue)) {
                    logger.warn(argument.wrongReason(stringValue))
                    return null
                }
                return argument.buildResult(stringValue, context)
            }

            when (editType) {
                GroupEditFlagArgument.TYPES.MIN_ONLINE_SERVICES -> {
                    val value = convertValueToInt(IntArgument("value", minValue = 0)) ?: return@syntax
                    if (value > group.data.maxOnlineService) {
                        i18n.info("agent.terminal.command.group.edit.warn.above-max", editType)
                        return@syntax
                    }
                    group.data.minOnlineService = value
                }
                GroupEditFlagArgument.TYPES.MAX_ONLINE_SERVICES -> {
                    val value = convertValueToInt(IntArgument("value", minValue = 0)) ?: return@syntax
                    if (value < group.data.minOnlineService) {
                        i18n.info("agent.terminal.command.group.edit.warn.below-min", editType)
                        return@syntax
                    }
                    group.data.maxOnlineService = value
                }
                GroupEditFlagArgument.TYPES.MIN_MEMORY -> {
                    val value = convertValueToInt(IntArgument("value", minValue = 1)) ?: return@syntax
                    if (value > group.data.maxMemory) {
                        i18n.info("agent.terminal.command.group.edit.warn.above-max", editType)
                        return@syntax
                    }
                    group.data.minMemory = value
                }
                GroupEditFlagArgument.TYPES.MAX_MEMORY -> {
                    val value = convertValueToInt(IntArgument("value", minValue = 1)) ?: return@syntax
                    if (value < group.data.minMemory) {
                        i18n.info("agent.terminal.command.group.edit.warn.below-min", editType)
                        return@syntax
                    }
                    group.data.maxMemory = value
                }
            }

            group.update()
            i18n.info("agent.terminal.command.group.edit.successful", group.data.name, editType.name, stringValue)
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
        val minOnlineServices = IntArgument("minOnlineServices", minValue = 0)
        val maxOnlineServices = IntArgument("maxOnlineServices", minValue = 0)
        val minMemory = IntArgument("minMemory", minValue = 1)
        val maxMemory = IntArgument("maxMemory", minValue = 1)

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