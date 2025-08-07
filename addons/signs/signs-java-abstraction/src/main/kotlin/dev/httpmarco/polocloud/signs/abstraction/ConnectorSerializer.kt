package dev.httpmarco.polocloud.signs.abstraction

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignLayout
import java.lang.reflect.Type

class ConnectorSerializer(val connectors: Connectors<*>) : JsonSerializer<BasedConnectorData<*>>,
    JsonDeserializer<BasedConnectorData<*>> {

    override fun serialize(
        src: BasedConnectorData<*>,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val obj = JsonObject()

        obj.addProperty(
            "type", when (src) {
                is SignData -> "SIGN"
                else -> "unknown"
            }
        )

        obj.addProperty("group", src.group)
        obj.add("position", context.serialize(src.position))
        obj.add("layout", JsonPrimitive(src.layout.id))

        return obj
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BasedConnectorData<*>? {
        val obj = json.asJsonObject

        val type = obj.get("type")?.asString ?: "unknown"
        val group = obj.get("group").asString
        val position = context.deserialize<Position>(obj.get("position"), Position::class.java)
        val layout = connectors.findLayout(obj.get("layout").asString) ?: throw IllegalArgumentException("Unbekannter Layout-Typ: ${obj.get("layout").asString}")

        return when (type) {
            "SIGN" -> SignData(group, position, layout as SignLayout)
            else -> null
        }
    }
}
