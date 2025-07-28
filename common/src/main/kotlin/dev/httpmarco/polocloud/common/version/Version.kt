package dev.httpmarco.polocloud.common.version

fun polocloudVersion() : String {
    return System.getenv("polocloud-version")
}