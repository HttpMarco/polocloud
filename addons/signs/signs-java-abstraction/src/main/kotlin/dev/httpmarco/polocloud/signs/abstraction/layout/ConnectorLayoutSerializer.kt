package dev.httpmarco.polocloud.signs.abstraction.layout

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import dev.httpmarco.polocloud.signs.abstraction.Connectors
import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerLayout
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignLayout
import java.lang.reflect.Type

class ConnectorLayoutSerializer : JsonSerializer<ConnectorLayout<*>>, JsonDeserializer<ConnectorLayout<*>> {

    override fun serialize(
        src: ConnectorLayout<*>,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val data = context.serialize(src).asJsonObject
        data.addProperty("type", src.typeId())
        return data
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ConnectorLayout<*>? {
        val data = json.asJsonObject
        val type = data.get("type")?.asString ?: throw IllegalArgumentException("ConnectorLayout must have a 'type' property")

        return when (type) {
            "SIGN" -> {
                context.deserialize<SignLayout>(data, SignLayout::class.java)
            }
            "BANNER" -> {
                context.deserialize<BannerLayout>(data, BannerLayout::class.java)
            }
            else -> {
                throw IllegalArgumentException("ConnectorLayout must have a 'type' property")
            }
        }
    }
}