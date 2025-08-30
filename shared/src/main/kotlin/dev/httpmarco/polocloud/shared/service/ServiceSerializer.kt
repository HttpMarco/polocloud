package dev.httpmarco.polocloud.shared.service

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import dev.httpmarco.polocloud.shared.template.Template
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceState
import java.lang.reflect.Type

class ServiceSerializer : JsonSerializer<Service>, JsonDeserializer<Service> {

    override fun serialize(
        src: Service,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement? {
        val data = JsonObject()

        data.addProperty("name", src.groupName)
        data.addProperty("id", src.id)
        data.addProperty("hostname", src.hostname)
        data.addProperty("port", src.port)
        data.addProperty("state", src.state.name)
        data.addProperty("type", src.type.name)
        data.add("templates", context.serialize(src.templates))
        data.addProperty("information", src.information.toString())
        data.addProperty("minMemory", src.minMemory)
        data.addProperty("maxMemory", src.maxMemory)
        data.addProperty("maxPlayerCount", src.maxPlayerCount)
        data.addProperty("playerCount", src.playerCount)
        data.addProperty("memoryUsage", src.memoryUsage)
        data.addProperty("cpuUsage", src.cpuUsage)
        data.addProperty("motd", src.motd)

        data.add("properties", context.serialize(src.properties.map { it.key to it.value }.toMap()))

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
        val state = ServiceState.valueOf(data.get("state").asString)
        val templatesType = object : TypeToken<List<Template>>() {}.type
        val templates = context.deserialize<List<Template>>(data.get("templates"), templatesType)
        val information = ServiceInformation.bindString(data.get("information").asString)
        val minMemory = data.get("minMemory").asInt
        val maxMemory = data.get("maxMemory").asInt
        val maxPlayerCount = data.get("maxPlayerCount").asInt
        val playerCount = data.get("playerCount").asInt
        val memoryUsage = data.get("memoryUsage").asDouble
        val cpuUsage = data.get("cpuUsage").asDouble
        val motd = data.get("motd").asString

        val propertiesType = object : TypeToken<Map<String, String>>() {}.type
        val properties = context.deserialize<Map<String, String>>(data.get("properties"), propertiesType)

        return Service(
            name,
            id,
            state,
            type,
            properties,
            hostname,
            port,
            templates,
            information,
            minMemory,
            maxMemory,
            playerCount,
            maxPlayerCount,
            memoryUsage,
            cpuUsage,
            motd
        )
    }
}