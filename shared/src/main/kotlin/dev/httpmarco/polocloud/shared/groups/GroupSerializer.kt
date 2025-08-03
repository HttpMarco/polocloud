package dev.httpmarco.polocloud.shared.groups

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


class GroupSerializer : JsonSerializer<Group>, JsonDeserializer<Group> {

    override fun serialize(
        src: Group?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        TODO("Not yet implemented")
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Group? {
        TODO("Not yet implemented")
    }
}