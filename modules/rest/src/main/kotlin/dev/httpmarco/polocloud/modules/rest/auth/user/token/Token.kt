package dev.httpmarco.polocloud.modules.rest.auth.user.token

import com.google.gson.JsonObject

data class Token(
    val value: String,
    val data: TokenData
)

fun Token.toJson(): JsonObject {
    return JsonObject().apply {
        addProperty("value", value)
        addProperty("ip", data.ip)
        addProperty("userUUID", data.userUUID.toString())
        addProperty("userAgent", data.userAgent)
        addProperty("lastActivity", data.lastActivity)
    }
}