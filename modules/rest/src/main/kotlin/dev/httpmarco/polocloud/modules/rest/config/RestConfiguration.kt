package dev.httpmarco.polocloud.modules.rest.config

data class RestConfiguration(
    val hostname: String = "0.0.0.0",
    val port: Int = 8080,
    val securePort: Int = 443,
    val sslEnabled: Boolean = false,
    val certPath: String = "",
    val keyPath: String = ""
) : Config