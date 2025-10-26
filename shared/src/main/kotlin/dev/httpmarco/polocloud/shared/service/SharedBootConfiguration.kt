package dev.httpmarco.polocloud.shared.service

import dev.httpmarco.polocloud.shared.template.Template

class SharedBootConfiguration {

    private var minMemory: Int? = null
    private var maxMemory: Int? = null
    private val templates = arrayListOf<Template>()
    private val excludedTemplates = arrayListOf<Template>()
    private val properties = hashMapOf<String, String>()

    fun minMemory(): Int? = minMemory
    fun maxMemory(): Int? = maxMemory
    fun templates(): List<Template> = templates
    fun excludedTemplates(): List<Template> = excludedTemplates
    fun properties(): Map<String, String> = properties

    fun withMinMemory(minMemory: Int): SharedBootConfiguration {
        this.minMemory = minMemory
        return this
    }

    fun withMaxMemory(minMemory: Int): SharedBootConfiguration {
        this.maxMemory = minMemory
        return this
    }

    fun withTemplate(template: Template): SharedBootConfiguration {
        this.templates += template
        return this
    }

    fun withTemplates(vararg template: Template): SharedBootConfiguration {
        this.templates += template
        return this
    }

    fun withExcludedTemplate(template: Template): SharedBootConfiguration {
        this.excludedTemplates += template
        return this
    }

    fun withExcludedTemplates(vararg template: Template): SharedBootConfiguration {
        this.excludedTemplates += template
        return this
    }

    fun withProperty(key: String, value: String): SharedBootConfiguration {
        this.properties[key] = value
        return this
    }
}