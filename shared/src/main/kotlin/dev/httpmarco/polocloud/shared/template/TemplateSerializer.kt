package dev.httpmarco.polocloud.shared.template

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


class TemplateSerializer : JsonSerializer<Template>, JsonDeserializer<Template> {

    override fun serialize(
        src: Template,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        val data = JsonObject()

        data.addProperty("name", src.name)
        data.addProperty("size", src.size)

        return data
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Template? {
        val data = json.asJsonObject

        val name = data.get("name").asString
        val size = data.get("size").asDouble

        return Template(
            name,
            size
        )
    }
}