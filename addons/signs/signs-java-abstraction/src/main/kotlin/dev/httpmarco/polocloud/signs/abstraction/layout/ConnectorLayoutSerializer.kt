package dev.httpmarco.polocloud.signs.abstraction.layout

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ConnectorLayoutSerializer : JsonSerializer<ConnectorLayout<*>>, JsonDeserializer<ConnectorLayout<*>> {

    override fun serialize(
        src: ConnectorLayout<*>,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val data = context.serialize(src).asJsonObject
        data.addProperty("type", src::class.java.simpleName.replace("Layout", "").uppercase())
        return data
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ConnectorLayout<*>? {
        TODO("Not yet implemented")
    }
}