package dev.httpmarco.polocloud.shared.service

class SharedBootConfiguration {

    private var minMemory: Int? = null
    private var maxMemory: Int? = null
    private val templates = arrayListOf<String>()
    private val excludedTemplates = arrayListOf<String>()
    private val properties = hashMapOf<String, String>()

    fun minMemory(): Int? = minMemory
    fun maxMemory(): Int? = maxMemory
    fun templates(): List<String> = templates
    fun excludedTemplates(): List<String> = excludedTemplates
    fun properties(): Map<String, String> = properties

    fun withMinMemory(minMemory: Int): SharedBootConfiguration {
        this.minMemory = minMemory
        return this
    }

    fun withMaxMemory(minMemory: Int): SharedBootConfiguration {
        this.maxMemory = minMemory
        return this
    }

    fun withTemplate(template: String): SharedBootConfiguration {
        this.templates += template
        return this
    }

    fun withTemplates(vararg template: String): SharedBootConfiguration {
        this.templates += template
        return this
    }

    fun withExcludedTemplate(template: String): SharedBootConfiguration {
        this.excludedTemplates += template
        return this
    }

    fun withExcludedTemplates(vararg template: String): SharedBootConfiguration {
        this.excludedTemplates += template
        return this
    }

    fun withProperty(key: String, value: String): SharedBootConfiguration {
        this.properties[key] = value
        return this
    }
}