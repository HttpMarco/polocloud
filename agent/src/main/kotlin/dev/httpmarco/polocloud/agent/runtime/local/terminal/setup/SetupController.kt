package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup

import dev.httpmarco.polocloud.agent.runtime.local.terminal.JLine3Terminal

class SetupController(private val terminal: JLine3Terminal) {

    private var displayedSetup: Setup<*>? = null

    fun start(setup: Setup<*>) {
        if (displayedSetup != null) {
            return
        }

        this.displayedSetup = setup
        displayedSetup?.start(terminal)
    }

    fun active(): Boolean {
        return displayedSetup != null
    }

    fun currentSetup(): Setup<*>? {
        return displayedSetup
    }

    fun completeCurrentSetup() {
        displayedSetup = null
    }
}