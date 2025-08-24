package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeTemplates
import dev.httpmarco.polocloud.shared.template.Template
import dev.httpmarco.polocloud.v1.GroupType
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

class LocalRuntimeTemplates : RuntimeTemplates<LocalService> {

    private val TEMPLATE_PATH = Path("local/templates")

    init {
        GroupType.entries.forEach {
            TEMPLATE_PATH.resolve("EVERY_$it").createDirectories()
        }
        // default template directory for all groups
        TEMPLATE_PATH.resolve("EVERY").createDirectories()
    }

    override fun bindTemplate(service: LocalService) {
        service.templates.forEach {
            val sourcePath = TEMPLATE_PATH.resolve(it)
            if (!Files.exists(sourcePath)) {
                sourcePath.createDirectories()
                // no template found, create empty directory
            }
            copyDirectory(sourcePath, service.path)
        }
    }

    override fun saveTemplate(template: String, service: LocalService) {
       copyDirectory(service.path, TEMPLATE_PATH.resolve(template))
    }

    override fun templates(service: LocalService): List<Template> {
        val templates = mutableListOf<Template>()
        Files.list(TEMPLATE_PATH).forEach { dir ->
            if (Files.isDirectory(dir)) {
                templates.add(Template(dir.fileName.toString(), folderSize(dir).toDouble() / (1024 * 1024)))
            }
        }

        return templates
    }

    override fun create(name: String) {
        val sourcePath = TEMPLATE_PATH.resolve(name)
        if (!Files.exists(sourcePath)) {
            sourcePath.createDirectories()
            // no template found, create empty directory
        }
    }

    override fun delete(name: String) {
        val sourcePath = TEMPLATE_PATH.resolve(name)
        Files.deleteIfExists(sourcePath)
    }

    override fun update(oldName: String, newName: String) {
        val sourcePath = TEMPLATE_PATH.resolve(oldName)
        val targetPath = TEMPLATE_PATH.resolve(newName)

        if (!Files.exists(sourcePath)) return
        if (Files.exists(targetPath)) {
            logger.warn("Template $newName already exists")
            return
        }

        try {
            Files.move(
                sourcePath,
                targetPath,
                StandardCopyOption.ATOMIC_MOVE
            )
            logger.info("Renamed template $oldName to $newName")
        } catch (e: IOException) {
            logger.warn("Cannot rename template $oldName to $newName: ${e.message}")
        }
    }

    private fun folderSize(path: Path): Long {
        var size = 0L
        Files.walk(path).forEach { if (Files.isRegularFile(it)) size += Files.size(it) }
        return size
    }

    fun copyDirectory(sourcePath: Path, targetPath: Path) {
        Files.walkFileTree(sourcePath, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                val targetDir = targetPath.resolve(sourcePath.relativize(dir))
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir)
                }
                return FileVisitResult.CONTINUE
            }

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                try {
                    Files.copy(
                        file,
                        targetPath.resolve(sourcePath.relativize(file)),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                } catch (e: IOException) {
                    logger.warn("Cannot copy file ${file.fileName}: ${e.message}")
                }
                return FileVisitResult.CONTINUE
            }
        })
    }
}
