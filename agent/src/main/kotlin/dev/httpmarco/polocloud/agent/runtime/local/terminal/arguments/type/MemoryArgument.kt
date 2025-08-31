package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext

class MemoryArgument(
    key: String,
    private val previousArg: MemoryArgument? = null
) : IntArgument(key, 1) {


    override fun buildResult(input: String, context: InputContext): Int {
        val value = input.toInt()
        lastEnteredMemory = value
        return value
    }

    override fun defaultArgs(context: InputContext): MutableList<String> {
        val suggestions = mutableListOf<String>()

        previousArg?.let { prev ->
            if (context.contains(prev)) {
                suggestions.add(context.arg(prev).toString())
            }
        }

        lastEnteredMemory?.let { last ->
            suggestions.add(last.toString())
        }

        return suggestions.distinct().toMutableList()
    }

    companion object {
        @Volatile
        private var lastEnteredMemory: Int? = null
    }


}