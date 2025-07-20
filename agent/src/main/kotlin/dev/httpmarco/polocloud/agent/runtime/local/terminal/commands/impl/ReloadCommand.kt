package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command

class ReloadCommand : Command("reload", "Reloads the agent configuration") {

    init {
        defaultExecution {
            i18n.info("agent.terminal.command.reload")

            Agent.instance.runtime.groupStorage().reload()

            i18n.info("agent.terminal.command.reload.successful")
        }
    }
}