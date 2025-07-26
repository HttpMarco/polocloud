package dev.httpmarco.polocloud.common.os

enum class OS(val nativeExecutableSuffix: String?) {
    WIN("exe"),
    LINUX(null),
    MACOS(null),
    UNKNOWN(null)
}