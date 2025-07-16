package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands

class CommandContext {
    private val contexts = HashMap<String, Any?>()

    @Suppress("UNCHECKED_CAST")
    fun <T> arg(argument: CommandArgument<T>): T {
        return contexts[argument.key] as T
    }

    fun append(argument: CommandArgument<*>, value: Any?) {
        this.contexts.put(argument.key, value)
    }
}