package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.exitPolocloud
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command

class ShutdownCommand : Command(i18n.get("agent.terminal.command.shutdown.name"), i18n.get("agent.terminal.command.shutdown.description"), i18n.get("agent.terminal.command.shutdown.alias.1")) {

    init {
        defaultExecution {
            exitPolocloud()
        }
    }

}