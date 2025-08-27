package dev.httpmarco.polocloud.shared.template

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


class TemplateSerializer : JsonSerializer<Template>, JsonDeserializer<Template> {

    override fun serialize(
        src: Template,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.name)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Template {
        return Template(json.asJsonPrimitive.asString)
    }
}