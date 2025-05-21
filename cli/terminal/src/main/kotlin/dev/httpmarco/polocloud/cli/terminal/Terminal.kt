package dev.httpmarco.polocloud.cli.terminal

import com.github.ajalt.mordant.input.InteractiveSelectListBuilder
import com.github.ajalt.mordant.input.interactiveSelectList
import com.github.ajalt.mordant.input.receiveEvents
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.danger
import com.github.ajalt.mordant.terminal.prompt
import com.github.ajalt.mordant.terminal.success

class Terminal {


    val terminal : Terminal;

    init {

        terminal = Terminal();

        val selection = terminal.interactiveSelectList {
            addEntry("Small")
            addEntry("Medium")
            addEntry("Large")
            title("Select Pizza Size")
        }
        if (selection == null) {
            terminal.danger("Aborted pizza order")
        } else {
            terminal.success("You ordered a $selection pizza")
        }

    }

    fun close() {

    }

    fun log(message: String) {
        terminal.println(message)
    }
}