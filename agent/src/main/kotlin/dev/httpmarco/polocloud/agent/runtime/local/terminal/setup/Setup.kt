package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.terminal.JLine3Terminal
import dev.httpmarco.polocloud.agent.runtime.local.terminal.LoggingColor
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext

abstract class Setup<T>(private val name: String) {

    private lateinit var terminal: JLine3Terminal
    private val steps: ArrayDeque<SetupStep<*>> = ArrayDeque();
    val context = InputContext()

    abstract fun bindQuestion()

    abstract fun onComplete(result: InputContext): T

    fun attach(step: SetupStep<*>) {
        steps.add(step)
    }

    fun start(terminal: JLine3Terminal) {
        this.bindQuestion()
        this.terminal = terminal
        this.terminal.updatePrompt("&7$name&8 » &7")
        this.display()
    }

    fun next() {
        if (steps.size <= 1) {
            this.onComplete(context)
            this.terminal.setupController.completeCurrentSetup()

            this.terminal.clearScreen()
            this.terminal.resetPrompt()
            i18n.info("agent.local-runtime.setup.completed", this.name)
            return
        }

        this.steps.removeFirst()
        this.display()
    }

    private fun display(wrongAnswer: Boolean = false) {
        val current = step()
        val translatedQuestion = i18n.get(current.questionKey)

        terminal.clearScreen()
        terminal.display("")
        terminal.display(LoggingColor.translate("&8 > &f$translatedQuestion"))

        val defaultArgs = current.argument.defaultArgs(context).filter { it != current.argument.key }
        if (defaultArgs.isNotEmpty()) {
            terminal.display(LoggingColor.translate("&8 > &7Possible answers: &e" + defaultArgs.joinToString("&8, &e")))
        }

        if(wrongAnswer) {
            terminal.display("")
            terminal.display(LoggingColor.translate("&8 > &cThe answer you provided is not valid."))
        }

        terminal.display("")
    }

    fun acceptAnswer(answer: String) {
        val argument = step().argument

        if (!argument.predication(answer)) {
            this.display(true)
            return
        }

        this.context.append(argument, argument.buildResult(answer, context))
        this.next()
    }

    fun step() : SetupStep<*> {
        return steps.first()
    }
}