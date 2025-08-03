package dev.httpmarco.polocloud.common.filesystem

import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries

fun copyDirectoryContent(source: Path, destination: Path, vararg options: CopyOption) {
    if (!source.isDirectory()) return

    source.listDirectoryEntries().forEach { file ->
        val destinationFile = destination.resolve(file.fileName)
        if (file.isDirectory()) {
            destinationFile.toFile().mkdirs()
            copyDirectoryContent(file, destinationFile, *options)
        } else {
            Files.copy(file, destinationFile, *options)
        }
    }
}