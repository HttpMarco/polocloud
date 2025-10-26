package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.shared.template.Template
import java.nio.file.Files
import java.nio.file.Path

class LocalTemplate(name: String) : Template(name, "unknown") {

    override fun size(): String {
        val sizeInBytes = folderSizeBytes(LOCAL_TEMPLATE_PATH.resolve(name))
        return humanReadableSize(sizeInBytes)
    }

    private fun folderSizeBytes(path: Path): Long {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return 0L
        }
        
        var size = 0L
        try {
            Files.walk(path).forEach { if (Files.isRegularFile(it)) size += Files.size(it) }
        } catch (e: Exception) {
            return 0L
        }
        return size
    }
}
