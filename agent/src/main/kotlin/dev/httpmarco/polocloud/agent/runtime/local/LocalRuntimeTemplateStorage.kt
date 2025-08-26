package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeTemplateStorage
import dev.httpmarco.polocloud.shared.template.Template
import dev.httpmarco.polocloud.v1.GroupType
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.CompletableFuture
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

class LocalRuntimeTemplateStorage : RuntimeTemplateStorage<Template, LocalService> {

    private val TEMPLATE_PATH = Path("local/templates")
    private val cachedTemplates = mutableListOf<Template>()

    init {
        GroupType.entries.forEach {
            TEMPLATE_PATH.resolve("EVERY_${it.name}").createDirectories()
        }
        // default template directory for all groups
        TEMPLATE_PATH.resolve("EVERY").createDirectories()

        refreshTemplateCache()
    }

    private fun refreshTemplateCache() {
        this.cachedTemplates.clear()

        Files.list(TEMPLATE_PATH).forEach { dir ->
            if (Files.isDirectory(dir)) {
                val name = dir.fileName.toString()
                this.cachedTemplates.add(Template(name, folderSize(dir)))
            }
        }

        GroupType.entries.forEach { type ->
            val groupPath = TEMPLATE_PATH.resolve("EVERY_${type.name}")
            if (Files.exists(groupPath)) {
                Files.list(groupPath).forEach { dir ->
                    if (Files.isDirectory(dir)) {
                        val name = dir.fileName.toString()
                        this.cachedTemplates.add(Template(name, folderSize(dir)))
                    }
                }
            }
        }
    }

    override fun bindTemplate(service: LocalService) {
        service.templates.forEach {
            val sourcePath = templatePath(it)
            if (!Files.exists(sourcePath)) {
                sourcePath.createDirectories()
                // no template found, create empty directory
            }
            copyDirectory(sourcePath, service.path)
        }
    }

    override fun saveTemplate(template: Template, service: LocalService) {
        copyDirectory(service.path, templatePath(template))
    }

    override fun templates(service: LocalService): List<Template> {
        return this.cachedTemplates.toList()
    }

    override fun create(template: Template) {
        val sourcePath = templatePath(template)
        if (!Files.exists(sourcePath)) {
            sourcePath.createDirectories()
            this.cachedTemplates.add(template)
        }
    }

    override fun delete(template: Template) {
        val path = templatePath(template)
        if (!Files.exists(path)) {
            return
        }

        Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                Files.delete(file)
                return FileVisitResult.CONTINUE
            }

            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                Files.delete(dir)
                return FileVisitResult.CONTINUE
            }
        })

        this.cachedTemplates.removeIf { it.name == template.name }
    }

    override fun update(template: Template, newName: String) {
        val sourcePath = templatePath(template)
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
            logger.info("Renamed template ${template.name} to $newName")
            this.cachedTemplates.removeIf { it.name == template.name }
            this.cachedTemplates.add(Template(newName, folderSize(targetPath)))
        } catch (e: IOException) {
            logger.warn("Cannot rename template ${template.name} to $newName: ${e.message}")
        }
    }

    private fun folderSize(path: Path): Double {
        var size = 0L
        Files.walk(path).forEach { if (Files.isRegularFile(it)) size += Files.size(it) }
        return size.toDouble() / (1024 * 1024)
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

    override fun findAll(): List<Template> {
        return this.cachedTemplates
    }

    override fun findAllAsync(): CompletableFuture<List<Template>> = CompletableFuture.completedFuture(findAll())

    override fun find(name: String): Template? {
        return this.cachedTemplates.find { it.name == name }
    }

    override fun findAsync(name: String): CompletableFuture<Template?> = CompletableFuture.completedFuture(find(name))

    private fun templatePath(template: Template): Path {
        return TEMPLATE_PATH.resolve(template.name)
    }
}
