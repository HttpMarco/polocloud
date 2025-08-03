package dev.httpmarco.polocloud.platforms

import com.google.gson.JsonElement

data class PlatformVersion(val version: String, val additionalProperties: Map<String, JsonElement> = emptyMap()) {
    val buildId: String?
        get() = additionalProperties["buildId"]?.asJsonPrimitive?.asString

    val requiredRuntimeVersion: String?
        get() = additionalProperties["requiredRuntimeVersion"]?.asJsonPrimitive?.asString

    fun getProperty(key: String): JsonElement? = additionalProperties[key]

    fun getStringProperty(key: String): String? = additionalProperties[key]?.asJsonPrimitive?.asString

    fun getBooleanProperty(key: String): Boolean? =
        additionalProperties[key]?.asJsonPrimitive?.asBoolean

    fun getIntProperty(key: String): Int? = additionalProperties[key]?.asJsonPrimitive?.asInt
}