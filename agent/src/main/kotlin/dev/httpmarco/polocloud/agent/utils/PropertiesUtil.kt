package dev.httpmarco.polocloud.agent.utils
import kotlinx.serialization.json.*

fun Map<String, JsonElement>.asStringMap(): Map<String, String> {
    return this.mapValues { (_, value) ->
        when (value) {
            is JsonPrimitive if value.isString -> value.content
            is JsonPrimitive -> value.toString()
            else -> Json.encodeToString(JsonElement.serializer(), value)
        }
    }
}