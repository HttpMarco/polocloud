package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.exitPolocloud
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.KeywordArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.updater.Updater

class UpdaterCommand : Command("updater", "Updates the agent to the latest version") {

    init {
        syntax(execution = {
            val versions = Updater.availableVersions()
            val current = polocloudVersion()

            logger.info("Available versions: ${versions.size}")

            if (!versions.contains(current)) {
                logger.info(" &8- &b${current}&8 (&7experimental version&8)")
            }

            versions.forEach { version ->
                logger.info(" &8- &7${version}&8" + (Updater.latestVersion() == version).let { if (it) " &8(&7latest&8)" else "" })
            }
        }, KeywordArgument("versions"))

        syntax(execution = {
            if(Updater.newVersionAvailable()) {
                logger.info("New version is available: &b${Updater.latestVersion()}&8 (&7Use&8: &3updater update&8)")
            } else {
                logger.info("You are already using the latest version: &b${polocloudVersion()}")
                logger.info("No update is required.")
            }
        }, KeywordArgument("check"))

        syntax(execution = {
            if(!Updater.newVersionAvailable()) {
                logger.info("You are already using the latest version: &b${polocloudVersion()}")
                return@syntax
            }
            exitPolocloud(shouldUpdate = true)
        }, KeywordArgument("update"))

    }
}