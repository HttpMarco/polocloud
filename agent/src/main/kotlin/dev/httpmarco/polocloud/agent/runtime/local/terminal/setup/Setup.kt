package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup

import java.util.LinkedList

abstract class Setup<T>(private val name: String) {

    private val steps = LinkedList<SetupStep<*>>()

    abstract fun bindQuestion()

    abstract fun onComplete(result: T)

}