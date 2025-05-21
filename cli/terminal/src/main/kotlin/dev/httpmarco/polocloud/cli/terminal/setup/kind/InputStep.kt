package dev.httpmarco.polocloud.cli.terminal.setup.kind

import com.github.ajalt.mordant.terminal.prompt
import dev.httpmarco.polocloud.cli.terminal.Terminal
import dev.httpmarco.polocloud.cli.terminal.setup.Step

class InputStep(question: String) : Step(question) {

    private val choices = mutableListOf<String>()

    override fun waitForAnswer(terminal: Terminal) {
        val response = terminal.terminal.prompt(question, choices=choices)
        terminal.log("You chose: $response")
    }

    /**
     * Adds a single choice to the current step.
     *
     * @param choice A string representing a single choice to be added.
     * @return The current `InputStep` instance to allow method chaining.
     */
    fun withChoices(choice: String) : InputStep {
        this.choices.add(choice)
        return this
    }

    /**
     * Adds a list of choices to the current step.
     *
     * @param choices A list of string choices to be added.
     * @return The current `InputStep` instance to allow method chaining.
     */
    fun withChoices(vararg choices: String) : InputStep {
        this.choices.addAll(choices)
        return this
    }
}