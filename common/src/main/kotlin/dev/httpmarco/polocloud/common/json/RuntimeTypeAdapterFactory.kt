package dev.httpmarco.polocloud.common.json

import com.google.gson.*
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class RuntimeTypeAdapterFactory<T> private constructor(
    private val baseType: Class<T>,
    private val typeFieldName: String,
    private val maintainType: Boolean
) : TypeAdapterFactory {

    private val labelToSubtype = mutableMapOf<String, Class<out T>>()
    private val subtypeToLabel = mutableMapOf<Class<out T>, String>()

    fun registerSubtype(subtype: Class<out T>, label: String = subtype.simpleName): RuntimeTypeAdapterFactory<T> {
        if (labelToSubtype.containsKey(label) || subtypeToLabel.containsKey(subtype)) {
            throw IllegalArgumentException("Types and labels must be unique.")
        }
        labelToSubtype[label] = subtype
        subtypeToLabel[subtype] = label
        return this
    }

    override fun <R : Any?> create(gson: Gson, type: TypeToken<R>): TypeAdapter<R>? {
        val rawType = type.rawType as Class<R>
        if (!baseType.isAssignableFrom(rawType)) return null

        val labelToDelegate = mutableMapOf<String, TypeAdapter<*>>()
        val subtypeToDelegate = mutableMapOf<Class<out T>, TypeAdapter<*>>()

        for ((label, subtype) in labelToSubtype) {
            val delegate = gson.getDelegateAdapter(this, TypeToken.get(subtype))
            labelToDelegate[label] = delegate
            subtypeToDelegate[subtype] = delegate
        }

        return object : TypeAdapter<R>() {
            override fun write(out: JsonWriter, value: R) {
                val srcType = value!!::class.java as Class<out T>
                val label = subtypeToLabel[srcType]
                    ?: throw JsonParseException("Cannot serialize $srcType; subtype not registered.")
                val delegate = subtypeToDelegate[srcType] as TypeAdapter<R>
                val jsonObject = delegate.toJsonTree(value).asJsonObject

                if (!maintainType) {
                    val clone = JsonObject()
                    clone.add(typeFieldName, JsonPrimitive(label))
                    for ((k, v) in jsonObject.entrySet()) {
                        clone.add(k, v)
                    }
                    Streams.write(clone, out)
                } else {
                    jsonObject.add(typeFieldName, JsonPrimitive(label))
                    Streams.write(jsonObject, out)
                }
            }

            override fun read(input: JsonReader): R {
                val jsonElement = Streams.parse(input)
                val jsonObject = jsonElement.asJsonObject
                val labelJsonElement = jsonObject.remove(typeFieldName)
                    ?: throw JsonParseException("Missing type field '$typeFieldName'")
                val label = labelJsonElement.asString
                val subtype = labelToSubtype[label]
                    ?: throw JsonParseException("Unknown type label '$label'")
                val delegate = labelToDelegate[label] as TypeAdapter<R>
                return delegate.fromJsonTree(jsonObject)
            }
        }
    }

    companion object {
        fun <T> of(baseType: Class<T>, typeFieldName: String = "type", maintainType: Boolean = false): RuntimeTypeAdapterFactory<T> {
            return RuntimeTypeAdapterFactory(baseType, typeFieldName, maintainType)
        }
    }
}
