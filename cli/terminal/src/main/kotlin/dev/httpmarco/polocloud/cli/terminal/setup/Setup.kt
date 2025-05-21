package dev.httpmarco.polocloud.cli.terminal.setup

import com.github.ajalt.mordant.terminal.info
import dev.httpmarco.polocloud.cli.terminal.Terminal

abstract class Setup {

    /**
     * A mutable list containing instances of the `Step` class that represent
     * the steps involved in the setup process.
     */
    private val steps = ArrayDeque<Step>()

    /**
     * Adds a step to the setup process by appending it to the list of steps.
     *
     * @param step The step to be added to the setup process.
     */
    protected fun append(step: Step) {
        steps.add(step)
    }

    /**
     * Displays the setup process by iterating over the list of steps.
     * This method is used to output or present the steps in a readable format.
     * The specific behavior of how the steps are displayed depends on the implementation.
     */
    fun display(terminal: Terminal) {
        this.next(terminal)
    }

    private fun next(terminal: Terminal) {
        if (steps.isEmpty()) {
            // if there are no more steps, we are done
            this.onCompleted()
            return
        }

        val step = steps.removeFirst();

        // display the current question in the terminal an appending the answer topology
        terminal.log(step.question)
        // wait for the user to answer
        step.waitForAnswer(terminal);
        // call the next question
        this.next(terminal)
    }

    /**
     * Invoked when the setup process is fully completed.
     * This method should be implemented to define the actions to be performed once
     * all the steps in the setup have been processed.
     */
    abstract fun onCompleted()

}