package dev.httpmarco.polocloud.common.os

//downloadName will be replace by FlorianLang06 later
enum class OS(val nativeExecutableSuffix: String?, val downloadName: String) {
    WIN("exe", "windows"),
    LINUX(null, "linux"),
    MACOS(null, "darwin"),
    UNKNOWN(null, "")
}