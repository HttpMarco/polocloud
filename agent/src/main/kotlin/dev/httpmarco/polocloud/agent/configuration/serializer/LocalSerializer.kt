package dev.httpmarco.polocloud.agent.configuration.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.Locale

class LocalSerializer : JsonSerializer<Locale>, JsonDeserializer<Locale> {

    override fun serialize(src: Locale, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.language)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Locale? {
        return Locale.forLanguageTag(json!!.asJsonPrimitive.asString)
    }
}