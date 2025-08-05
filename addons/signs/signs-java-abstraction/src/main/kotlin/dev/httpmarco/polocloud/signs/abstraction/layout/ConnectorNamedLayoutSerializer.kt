package dev.httpmarco.polocloud.signs.abstraction.layout

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ConnectorNamedLayoutSerializer : JsonSerializer<ConnectorLayout<*>>, JsonDeserializer<ConnectorLayout<*>> {

    override fun serialize(src: ConnectorLayout<*>, typeOfSrc: Type, context: JsonSerializationContext?): JsonElement? {
        return JsonPrimitive(src.typeId())
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ConnectorLayout<*>? {
        TODO("Not yet implemented")
    }
}