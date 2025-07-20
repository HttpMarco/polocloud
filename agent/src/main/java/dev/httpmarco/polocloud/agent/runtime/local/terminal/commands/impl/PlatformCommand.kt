package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.KeywordArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.PlatformArgument
import dev.httpmarco.polocloud.platforms.PlatformPool

class PlatformCommand() : Command("platform", "Manage the platforms") {

    init {
        syntax(execution = {
            if (PlatformPool.platforms().isEmpty()) {
                logger.info("No platform found.")
                return@syntax
            }
            logger.info("Found ${PlatformPool.size()} platforms&8:")
            PlatformPool.platforms().forEach { logger.info(" &8- &3${it.name} &8(&7versions&8=&7${it.versions.size} language&8=&7${it.language.name}&8)") }
        }, KeywordArgument("list"))


        val platformArg = PlatformArgument()
        syntax(execution = {

            val platform = it.arg(platformArg)

            logger.info("Service &3${platform.name}&8:")
            logger.info(" &8- &7Language&8: &f${platform.language}")
            logger.info(" &8- &7Url&8: &f${platform.url}")
            logger.info(" &8- &7Type&8: &f${platform.type}")
            logger.info(" &8- &7Arguments&8(&7${platform.arguments.size}&8):")
            platform.arguments.forEach { arg ->
                logger.info("   &8- &7${arg}")
            }
            logger.info(" &8- &7Tasks&8(&7${platform.tasks().size}&8):")
            platform.tasks().forEach { task ->
                logger.info("   &8- &7${task.name}")
            }

            logger.info(" &8- &7Versions&8(&7${platform.versions.size}&8):")
            platform.versions.forEach { version ->
                logger.info("   &8- &7${version.version} &8(&7${version.buildId}&8)")
            }
        }, platformArg)

        syntax(execution = {
            TODO()
        }, KeywordArgument("tasks"))

    }
}