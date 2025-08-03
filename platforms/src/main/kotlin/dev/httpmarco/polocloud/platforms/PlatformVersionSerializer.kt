package dev.httpmarco.polocloud.platforms

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class PlatformVersionSerializer : JsonDeserializer<PlatformVersion> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PlatformVersion? {
        val data = json.asJsonObject
        val version =
            data["version"]?.asJsonPrimitive?.asString ?: throw NullPointerException("version field is required")

        val additionalProperties = hashMapOf<String, JsonElement>()

        data.keySet().toList().filter { it != "version" }.forEach {
            additionalProperties[it] = data.get(it)
        }

        return PlatformVersion(version, additionalProperties)
    }
}