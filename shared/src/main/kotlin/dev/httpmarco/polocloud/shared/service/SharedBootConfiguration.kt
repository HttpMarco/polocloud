package dev.httpmarco.polocloud.shared.service

class SharedBootConfiguration {

    private var minMemory: Int? = null
    private var maxMemory: Int? = null
    private val templates = arrayListOf<String>()

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
}