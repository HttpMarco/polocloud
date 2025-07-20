package dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.TerminalArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext

class IntArgument(key: String, val minValue: Int? = null, val maxValue: Int? = null) : TerminalArgument<Int>(key) {
    override fun predication(rawInput: String): Boolean {
        return getError(rawInput) == null
    }

    override fun wrongReason(rawInput: String): String {
        return when (getError(rawInput)) {
            IntArgumentError.INVALID_INT -> i18n.get("agent.terminal.command.argument.type.int.wrong")
            IntArgumentError.LOWER_THAN_MIN -> i18n.get(
                "agent.terminal.command.argument.type.int.wrong.atleast",
                key,
                minValue
            )

            IntArgumentError.HIGHER_THAN_MAX -> i18n.get(
                "agent.terminal.command.argument.type.int.wrong.notexceed",
                key,
                maxValue
            )
            else -> ""
        }
    }

    override fun defaultArgs(context: InputContext): MutableList<String> {
        return mutableListOf()
    }

    override fun buildResult(input: String, context: InputContext): Int {
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
