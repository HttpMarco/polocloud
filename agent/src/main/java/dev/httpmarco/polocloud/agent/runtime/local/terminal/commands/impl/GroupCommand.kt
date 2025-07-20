package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.groups.GroupData
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
        syntax(execution = { context ->
            if (groupStorage.items().isEmpty()) {
                logger.info("No groups found.")
                return@syntax
            }
            logger.info("Found ${groupStorage.items().size} groups&8:")
            groupStorage.items()
                .forEach { logger.info(" &8- &3${it.data.name} &8(&7minOnlineServices&8=&7${it.data.minOnlineService} services&8=&7${it.serviceCount()}&8)") }
        }, KeywordArgument("list"))

        val groupArgument = GroupArgument()

        syntax(execution = { context ->
            var group = context.arg(groupArgument)

            logger.info("Group &3${group.data.name}&8:")
            logger.info(" &8- &7Min Memory&8: &f${group.data.minMemory}MB")
            logger.info(" &8- &7Max Memory&8: &f${group.data.maxMemory}MB")
            logger.info(" &8- &7Min Online Services&8: &f${group.data.minOnlineService}")
            logger.info(" &8- &7Max Online Services&8: &f${group.data.maxOnlineService}")
            logger.info(" &8- &7Online services&8: &f${group.serviceCount()}")
            logger.info(" &8- &7Platform&8: &f${group.data.platform.group} &8(&7${group.data.platform.version}&8)")
            logger.info(" &8- &7Properties&8:")

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
                    val value = convertValueToInt(IntArgument("value", minValue = 1)) ?: return@syntax
                    if (value > group.data.maxOnlineService) {
                        logger.warn("Group can't be updated because ${editType.name} can't be greater than max value.")
                        return@syntax
                    }
                    group.data.minOnlineService = value
                }
                GroupEditFlagArgument.TYPES.MAX_ONLINE_SERVICES -> {
                    val value = convertValueToInt(IntArgument("value", minValue = 1)) ?: return@syntax
                    if (value < group.data.minOnlineService) {
                        logger.warn("Group can't be updated because ${editType.name} can't be lower than min value.")
                        return@syntax
                    }
                    group.data.maxOnlineService = value
                }
                GroupEditFlagArgument.TYPES.MIN_MEMORY -> {
                    val value = convertValueToInt(IntArgument("value", minValue = 1)) ?: return@syntax
                    if (value > group.data.maxMemory) {
                        logger.warn("Group can't be updated because ${editType.name} can't be greater than max value.")
                        return@syntax
                    }
                    group.data.minMemory = value
                }
                GroupEditFlagArgument.TYPES.MAX_MEMORY -> {
                    val value = convertValueToInt(IntArgument("value", minValue = 1)) ?: return@syntax
                    if (value < group.data.minMemory) {
                        logger.warn("Group can't be updated because ${editType.name} can't be lower than min value.")
                        return@syntax
                    }
                    group.data.maxMemory = value
                }
            }

            group.update()
            logger.info("The group &f${group.data.name} &7has been edited&8: &7Update &3${editType.name} &7to &f$stringValue&8.")

        }, groupArgument, KeywordArgument("edit"), GroupEditFlagArgument(), TextArgument("value"))

        syntax(execution = { context ->
            var group = context.arg(groupArgument)
            logger.info("All services of groups will be shutdown&8...")
            group.shutdownAll()
            logger.info("All services of group ${group.data.name} are now shutdown&8.")
        }, groupArgument, KeywordArgument("shutdownAll"))

        syntax(execution = { context ->
            val group = context.arg(groupArgument)

            Agent.instance.runtime.groupStorage().destroy(group)
            group.shutdownAll()

            logger.info("Group ${group.data.name} deleted.")
        }, groupArgument, KeywordArgument("delete"))

        val nameArgument = TextArgument("name")
        val platformArgument = PlatformArgument()
        val platformVersionArgument = PlatformVersionArgument(platformArgument)
        val minOnlineServices = IntArgument("minOnlineServices", minValue = 1)
        val maxOnlineServices = IntArgument("maxOnlineServices", minValue = 1)
        val minMemory = IntArgument("minMemory", minValue = 1)
        val maxMemory = IntArgument("maxMemory", minValue = 1)

        syntax(
            execution = { context ->

                val minMemory = context.arg(minMemory)
                val maxMemory = context.arg(maxMemory)

                if (maxMemory < minMemory) {
                    logger.warn("Max memory cannot be less than min memory&8. &7Using min memory as max memory&8.")
                    return@syntax
                }

                val minOnlineServices = context.arg(minOnlineServices)
                val maxOnlineServices = context.arg(maxOnlineServices)

                if (maxOnlineServices < minOnlineServices) {
                    logger.warn("Max online services cannot be less than min online service&8. &7Using min online services as max online service&8.")
                    return@syntax
                }

                val platform = context.arg(platformArgument)
                val groupName = context.arg(nameArgument)

                if (Agent.instance.runtime.groupStorage().items()
                        .any { it.data.name.equals(groupName, ignoreCase = true) }
                ) {
                    logger.warn("Group with name $groupName already exists.")
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
                logger.info("Group &f$groupName successfully created&8.")
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