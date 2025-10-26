package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.common.version.runtimeVersion

fun checkRuntimeVersion(service: LocalService): Pair<Boolean, String?> {
    val requiredVersionValue = service.group().platform()
        .version(service.group().platform.version)
        ?.requiredRuntimeVersion ?: return true to null

    val currentVersion = runtimeVersion(service.group().platform().language)
    if (currentVersion.isEmpty()) return false to null

    val operatorRegex = Regex("""^(>=|<=|>|<|==)\s*(.+)""")
    val match = operatorRegex.find(requiredVersionValue)

    val (operator, requiredVersion) = if (match != null) {
        match.groupValues[1] to match.groupValues[2]
    } else {
        null to requiredVersionValue
    }

    return compareVersions(currentVersion, requiredVersion, operator) to currentVersion
}

private fun compareVersions(current: String, required: String, operator: String?): Boolean {
    if (operator == null) {
        return current.startsWith(required)
    }

    val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
    val requiredParts = required.split(".").map { it.toIntOrNull() ?: 0 }

    val minLength = minOf(currentParts.size, requiredParts.size)
    val currentNormalized = currentParts.take(minLength)
    val requiredNormalized = requiredParts.take(minLength)

    val comparison = compareVersionParts(currentNormalized, requiredNormalized)

    return when (operator) {
        ">=" -> comparison >= 0
        "<=" -> comparison <= 0
        ">" -> comparison > 0
        "<" -> comparison < 0
        "==" -> comparison == 0
        else -> comparison >= 0 // default: >=
    }
}

private fun compareVersionParts(current: List<Int>, required: List<Int>): Int {
    for (i in current.indices) {
        val comparison = current[i].compareTo(required[i])
        if (comparison != 0) return comparison
    }
    return 0
}