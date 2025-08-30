package dev.httpmarco.polocloud.shared.template

import dev.httpmarco.polocloud.v1.templates.TemplateSnapshot
import kotlin.math.pow

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

    protected fun humanReadableSize(bytes: Long): String {
        if (bytes <= 0) return "empty"

        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (kotlin.math.log10(bytes.toDouble()) / kotlin.math.log10(1024.0)).toInt()
        val humanValue = bytes / 1024.0.pow(digitGroups.toDouble())

        return String.format("%.1f %s", humanValue, units[digitGroups])
    }

    open fun size(): String {
        return size
    }
}