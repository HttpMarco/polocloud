package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.shared.template.Template
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.log10
import kotlin.math.pow

class LocalTemplate(name: String) : Template(name, "unknown") {

    override fun size(): String {
        val sizeInBytes = folderSizeBytes(LOCAL_TEMPLATE_PATH.resolve(name))
        if (sizeInBytes <= 0) return "empty"

        val units = arrayOf("b", "kb", "Mb", "Gb", "Tb")
        val digitGroups = (log10(sizeInBytes.toDouble()) / log10(1024.0)).toInt()
        val humanValue = sizeInBytes / 1024.0.pow(digitGroups.toDouble())

        return String.format("%.1f%s", humanValue, units[digitGroups])
    }

    private fun folderSizeBytes(path: Path): Long {
        var size = 0L
        Files.walk(path).forEach { if (Files.isRegularFile(it)) size += Files.size(it) }
        return size
    }
}
