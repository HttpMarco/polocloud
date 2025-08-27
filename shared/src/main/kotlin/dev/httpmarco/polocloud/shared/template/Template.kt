package dev.httpmarco.polocloud.shared.template

import dev.httpmarco.polocloud.v1.templates.TemplateSnapshot

open class Template(
    val name: String,
    private val size: String = "unknown"
) {

    companion object {
        fun fromSnapshot(proto: TemplateSnapshot): Template {
            return Template(proto.name, proto.size)
        }

        fun bindSnapshot(list: List<TemplateSnapshot>): List<Template> {
            return list.map { fromSnapshot(it) }
        }
    }

    fun toSnapshot(): TemplateSnapshot {
        return TemplateSnapshot.newBuilder()
            .setName(name)
            .setSize(size())
            .build()
    }

    open fun size(): String {
        return size
    }
}