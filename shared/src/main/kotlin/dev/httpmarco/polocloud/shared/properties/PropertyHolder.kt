package dev.httpmarco.polocloud.shared.properties

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement

private val propertySerializer = GsonBuilder().serializeNulls().create()

open class PropertyHolder(
    private val properties: MutableMap<String, JsonPrimitive> = mutableMapOf()
) {

    fun <T> hasProperty(property: Property<T>) : Boolean {
        return properties.containsKey(property.name)
    }

    fun <T> get(property: Property<T>) : T? {
        return properties[property.name] as T?
    }

    fun <T> with(property: Property<T>, value: T) : PropertyHolder {
        properties[property.name] = propertySerializer.toJsonTree(value)
        return this
    }

    fun <T> remove(property: Property<T>) {
        properties.remove(property.name)
    }

    fun raw(key: String, value: JsonElement) : PropertyHolder {
        properties[key] = value
        return this
    }

    fun all() : Map<String, JsonElement> {
        return properties
    }

    companion object {

        fun empty(): PropertyHolder {
            return PropertyHolder()
        }
    }
}