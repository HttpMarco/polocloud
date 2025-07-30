package dev.httpmarco.polocloud.shared.service

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import java.lang.reflect.Type

class ServiceSerializer : JsonSerializer<Service>, JsonDeserializer<Service> {

    override fun serialize(
        src: Service,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement? {
        val data = JsonObject()

        data.addProperty("name", src.name())
        data.addProperty("id", src.serviceSnapshot.id)
        data.addProperty("hostname", src.hostname())
        data.addProperty("port", src.port())
        data.addProperty("type", src.type().name)
        data.add("properties", context.serialize(src.properties().map { it -> it.key to it.value }.toMap()))

        return data
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Service {
        val data = json.asJsonObject

        val name = data.get("name").asString
        val id = data.get("id").asInt
        val hostname = data.get("hostname").asString
        val port = data.get("port").asInt
        val type = GroupType.valueOf(data.get("type").asString)

        val propertiesType = object : TypeToken<Map<String, String>>() {}.type
        val properties = context.deserialize<Map<String, String>>(data.get("properties"), propertiesType)

        return Service(
            ServiceSnapshot.newBuilder()
                .setGroupName(name)
                .setId(id)
                .setServerType(type)
                .setHostname(hostname)
                .setPort(port)
                .putAllProperties(properties)
                .build()
        )
    }


}