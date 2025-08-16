package dev.httpmarco.polocloud.shared.module

data class ModuleMetadata(
    val id: String,
    val name: String,
    val description: String,
    val author: String,
    val main: String
)