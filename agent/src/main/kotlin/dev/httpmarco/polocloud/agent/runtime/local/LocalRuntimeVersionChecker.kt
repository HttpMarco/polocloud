package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.platforms.PlatformLanguage

fun checkRuntimeVersion(service: LocalService): Pair<Boolean, String?> {
    val requiredVersionValue = service.group.platform()
        .version(service.group.platform.version)
        ?.requiredRuntimeVersion ?: return true to null

    val currentVersion = getRuntimeVersion(service.group.platform().language)
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

private fun getRuntimeVersion(language: PlatformLanguage): String {
    return when (language) {
        PlatformLanguage.JAVA -> getJavaVersion()
        PlatformLanguage.GO -> return "" // bleibt bei true
        PlatformLanguage.RUST -> getRustVersion()
    }
}

private fun getJavaVersion(): String {
    return try {
        val process = ProcessBuilder("java", "--version").start()
        val output = process.inputStream.bufferedReader().use { it.readText() }
        // Extrahiert Version aus: "openjdk 21.0.8 2025-07-15"
        val versionRegex = Regex("""openjdk\s+(\d+\.\d+\.\d+)""")
        versionRegex.find(output)?.groupValues?.get(1) ?: ""
    } catch (_: Exception) {
        ""
    }
}

private fun getRustVersion(): String {
    return try {
        val process = ProcessBuilder("rustc", "--version").start()
        val output = process.inputStream.bufferedReader().use { it.readText() }
        val versionRegex = Regex("""rustc\s+(\d+\.\d+\.\d+)""")
        versionRegex.find(output)?.groupValues?.get(1) ?: ""
    } catch (_: Exception) {
        ""
    }
}

private fun compareVersions(current: String, required: String, operator: String?): Boolean {
    if (operator == null) {
        return current.startsWith(required)
    }

    val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
    val requiredParts = required.split(".").map { it.toIntOrNull() ?: 0 }

    // Gleiche Länge für Vergleich
    val minLength = minOf(currentParts.size, requiredParts.size)
    println("Length: $minLength")
    val currentNormalized = currentParts.take(minLength)
    val requiredNormalized = requiredParts.take(minLength)

    val comparison = compareVersionParts(currentNormalized, requiredNormalized)
    println("$comparison $currentNormalized $requiredNormalized")

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