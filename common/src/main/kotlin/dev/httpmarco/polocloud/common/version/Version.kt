package dev.httpmarco.polocloud.common.version

import dev.httpmarco.polocloud.common.language.Language

fun polocloudVersion() : String {
    return System.getenv("POLOCLOUD_VERSION")
}

fun runtimeVersion(language: Language): String {
    return when (language) {
        Language.JAVA -> javaVersion()
        Language.GO -> return "" // bleibt bei true
        Language.RUST -> rustVersion()
    }
}

fun javaVersion(): String {
    return try {
        val process = ProcessBuilder("java", "--version").start()
        val output = process.inputStream.bufferedReader().use { it.readText() }
        val versionRegex = Regex("""openjdk\s+(\d+\.\d+\.\d+)""")
        versionRegex.find(output)?.groupValues?.get(1) ?: ""
    } catch (_: Exception) {
        ""
    }
}

fun rustVersion(): String {
    return try {
        val process = ProcessBuilder("rustc", "--version").start()
        val output = process.inputStream.bufferedReader().use { it.readText() }
        val versionRegex = Regex("""rustc\s+(\d+\.\d+\.\d+)""")
        versionRegex.find(output)?.groupValues?.get(1) ?: ""
    } catch (_: Exception) {
        ""
    }
}