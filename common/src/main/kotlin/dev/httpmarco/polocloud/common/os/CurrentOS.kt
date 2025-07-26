package dev.httpmarco.polocloud.common.os

val currentOS: OS by lazy {
    val osName = System.getProperty("os.name").lowercase()
    when {
        "windows" in osName -> OS.WIN
        "linux" in osName -> OS.LINUX
        "mac" in osName -> OS.MACOS
        else -> OS.UNKNOWN
    }
}