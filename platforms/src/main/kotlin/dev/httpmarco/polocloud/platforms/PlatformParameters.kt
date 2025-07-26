package dev.httpmarco.polocloud.platforms

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive

class PlatformParameters(private val version: PlatformVersion?) {
    val parameters = HashMap<String, Any>()
    val versionPrefix = "version_"

    inline fun <reified T> getParameter(key: String): T? {
        val value = parameters[key]
        if (value == null) {
            return null
        }
        return parameters[key] as T
    }

    fun getStringParameter(key: String): String? {
        if (key.startsWith(versionPrefix) && version != null) {
            return version.getStringProperty(key.substring(versionPrefix.length))
        }
        return parameters[key] as String
    }


    fun getIntParameter(key: String): Int? {
        if (key.startsWith(versionPrefix) && version != null) {
            return version.getIntProperty(key.substring(versionPrefix.length))
        }
        return parameters[key] as Int
    }


    fun getBooleanParameter(key: String): Boolean? {
        if (key.startsWith(versionPrefix) && version != null) {
            return version.getBooleanProperty(key.substring(versionPrefix.length))
        }
        return parameters[key] as Boolean
    }

    fun addParameter(key: String, value: Any) {
        parameters[key] = value
    }

    fun modifyValueWithEnvironment(value: String): String {
        var modifiedValue = value
        for ((envKey, envValue) in parameters) {
            modifiedValue = modifiedValue.replace("%$envKey%", "$envValue")
        }
        if (modifiedValue.contains("%$versionPrefix") && version != null) {
            modifiedValue = modifiedValue.replace(versionPrefix, "")
            for ((verKey, verValue) in version.additionalProperties) {
                modifiedValue = modifiedValue.replace("%$verKey%", verValue.jsonPrimitive.content)
            }
        }
        return modifiedValue
    }
}