package dev.httpmarco.polocloud.cli.installer

import dev.httpmarco.polocloud.cli.terminal.setup.Setup
import dev.httpmarco.polocloud.cli.terminal.setup.kind.InputStep

class InstallSetup : Setup() {

    init {
        append(InputStep("Test question?").withChoices("yes", "no"))
    }

    override fun onCompleted() {
        println("completed")
    }
}