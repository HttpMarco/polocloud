package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.impl

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.Setup
import dev.httpmarco.polocloud.platforms.Platform

class CustomPlatformSetup : Setup<Platform>("Custom platform setup") {

    override fun bindQuestion() {
        TODO("Not yet implemented")
    }

    override fun onComplete(result: InputContext): Platform {
        TODO("Not yet implemented")
    }
}