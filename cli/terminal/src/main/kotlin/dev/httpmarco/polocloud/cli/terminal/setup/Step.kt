package dev.httpmarco.polocloud.cli.terminal.setup

import dev.httpmarco.polocloud.cli.terminal.Terminal

abstract class Step(val question: String) {

    abstract fun waitForAnswer(terminal: Terminal)

}