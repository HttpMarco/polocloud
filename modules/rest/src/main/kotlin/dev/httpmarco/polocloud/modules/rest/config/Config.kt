package dev.httpmarco.polocloud.modules.rest.config

data class Config(
    val javalinConfiguration: JavalinConfig = JavalinConfig("0.0.0.0", 8080)
)

data class JavalinConfig(
    val hostname: String,
    val port: Int,
)