package dev.httpmarco.polocloud.platforms.tasks

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class PlatformTaskSerializer : JsonSerializer<PlatformTask>, JsonDeserializer<PlatformTask> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): PlatformTask? {
        return PlatformTaskPool.find(json.asJsonPrimitive.asString) ?: throw IllegalArgumentException("Unknown task type: ${json.asJsonPrimitive.asString}")
    }

    override fun serialize(
        src: PlatformTask,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement? {
        return JsonPrimitive(src.name)
    }
}