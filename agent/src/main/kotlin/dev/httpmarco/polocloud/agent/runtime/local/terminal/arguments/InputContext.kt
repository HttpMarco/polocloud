package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments

class InputContext {
    private val contexts = HashMap<String, Any?>()

    @Suppress("UNCHECKED_CAST")
    fun <T> arg(argument: TerminalArgument<T>): T {
        return contexts[argument.key] as T
    }

    fun append(argument: TerminalArgument<*>, value: Any?) {
        this.contexts[argument.key] = value
    }

    fun remove(argument: TerminalArgument<*>) {
        this.contexts.remove(argument.key)
    }
}