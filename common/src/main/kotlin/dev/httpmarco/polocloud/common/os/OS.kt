package dev.httpmarco.polocloud.common.os

enum class OS(
    val nativeExecutableSuffix: String?,
    val shellPrefix: Array<String>,
    val currentDirectoryPrefix: String?
) {
    WIN("exe",  arrayOf("cmd", "/c"), null),
    LINUX(null, arrayOf("sh", "-c"), "./"),
    MACOS(null, arrayOf("sh", "-c"), "./"),
    UNKNOWN(null, emptyArray(), null);

    fun executableCurrentDirectoryCommand(filename: String): Array<String> {
        return arrayOf(*shellPrefix, "${currentDirectoryPrefix ?: ""}$filename")
    }
}