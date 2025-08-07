package dev.httpmarco.polocloud.common.os

//downloadName will be replace by FlorianLang06 later
enum class OS(
    val nativeExecutableSuffix: String?,
    val downloadName: String,
    val shellPrefix: Array<String>,
    val currentDirectoryPrefix: String?
) {
    WIN("exe", "windows", arrayOf("cmd", "/c"), null),
    LINUX(null, "linux", arrayOf("sh", "-c"), "./"),
    MACOS(null, "darwin", arrayOf("sh", "-c"), "./"),
    UNKNOWN(null, "", emptyArray(), null);

    fun executableCurrentDirectoryCommand(filename: String): Array<String> {
        return arrayOf(*shellPrefix, "${currentDirectoryPrefix ?: ""}$filename")
    }
}