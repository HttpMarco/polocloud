package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.exitPolocloud
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command

class ShutdownCommand : Command("shutdown", "Shuts down the agent", "stop") {

    init {
        defaultExecution {
            exitPolocloud()
        }
    }

}