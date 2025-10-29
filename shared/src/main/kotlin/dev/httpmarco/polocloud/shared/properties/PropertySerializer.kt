package dev.httpmarco.polocloud.shared.properties

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


class PropertySerializer : JsonSerializer<PropertyHolder>, JsonDeserializer<PropertyHolder> {

    override fun serialize(
        src: PropertyHolder,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val json = JsonObject()
        src.all().forEach { (key, value) ->
            json.add(key, value)
        }
        return json
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): PropertyHolder {
        val holder = PropertyHolder.empty()
        json.asJsonObject.entrySet().forEach { (key, value) ->
            when {
                value.isJsonPrimitive -> holder.raw(key, value.asJsonPrimitive)
                value.isJsonNull -> holder.raw(key, JsonPrimitive("null"))
                value.isJsonObject || value.isJsonArray ->
                    holder.raw(key, JsonPrimitive(value.toString()))
            }
        }
        return holder
    }

}