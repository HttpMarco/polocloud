package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type

import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandContext

class IntArgument(key: String, val minValue: Int? = null, val maxValue: Int? = null) : CommandArgument<Int>(key) {
    override fun predication(rawInput: String): Boolean {
        return getError(rawInput) == null
    }

    override fun wrongReason(rawInput: String): String {
        //TODO needs to be replaced with translation keys
        return when (getError(rawInput)) {
            IntArgumentError.INVALID_INT -> "Input $key is not a valid integer."
            IntArgumentError.LOWER_THAN_MIN -> "Input $key must be at least $minValue"
            IntArgumentError.HIGHER_THAN_MAX -> "Input $key must not exceed $maxValue"
            else -> ""
        }
    }

    override fun defaultArgs(context: CommandContext): MutableList<String> {
        return mutableListOf()
    }

    override fun buildResult(input: String, context: CommandContext): Int {
        return input.toInt()
    }

    private fun getError(rawInput: String): IntArgumentError? {
        try {
            val intValue = rawInput.toInt()

            if (maxValue != null && intValue > maxValue) {
                return IntArgumentError.HIGHER_THAN_MAX
            }

            if (minValue != null && intValue < minValue) {
                return IntArgumentError.LOWER_THAN_MIN
            }

            return null
        } catch (_: NumberFormatException) {
            return IntArgumentError.INVALID_INT
        }
    }
}

enum class IntArgumentError {
    INVALID_INT,
    LOWER_THAN_MIN,
    HIGHER_THAN_MAX,
}
