package dev.httpmarco.polocloud.platforms.bridge

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import dev.httpmarco.polocloud.platforms.PlatformPool
import java.lang.reflect.Type

class BridgeSerializer : JsonSerializer<Bridge>, JsonDeserializer<Bridge> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Bridge? {
        return PlatformPool.findBindBridge(json.asJsonPrimitive.asString)
    }

    override fun serialize(
        src: Bridge,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.name)
    }
}