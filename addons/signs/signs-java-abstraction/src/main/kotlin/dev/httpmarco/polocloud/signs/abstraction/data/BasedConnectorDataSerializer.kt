package dev.httpmarco.polocloud.signs.abstraction.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout
import java.lang.reflect.Type

class BasedConnectorDataSerializer : JsonSerializer<ConnectorLayout<*>>, JsonDeserializer<ConnectorLayout<*>> {

    override fun serialize(
        src: ConnectorLayout<*>,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {

        var data = JsonObject()
        data
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