package dev.httpmarco.polocloud.shared.properties

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

private val propertySerializer = GsonBuilder().serializeNulls().create()

open class PropertyHolder(
    private val properties: MutableMap<String, JsonPrimitive> = mutableMapOf()
) {

    fun <T> hasProperty(property: Property<T>) : Boolean {
        return properties.containsKey(property.name)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(property: Property<T>) : T? {
        val jsonPrimitive = properties[property.name] ?: return null
        
        return when {
            jsonPrimitive.isBoolean -> jsonPrimitive.asBoolean as T
            jsonPrimitive.isNumber -> {
                // Try to determine if it's an Int or other number type
                val number = jsonPrimitive.asNumber
                when {
                    number is Int || number.toDouble() == number.toInt().toDouble() -> number.toInt() as T
                    else -> number as T
                }
            }
            jsonPrimitive.isString -> jsonPrimitive.asString as T
            else -> null
        }
    }

    fun <T> with(property: Property<T>, value: T) : PropertyHolder {
        properties[property.name] = propertySerializer.toJsonTree(value) as JsonPrimitive
        return this
    }

    fun <T> remove(property: Property<T>) {
        properties.remove(property.name)
    }

    fun raw(key: String, value: JsonPrimitive) : PropertyHolder {
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