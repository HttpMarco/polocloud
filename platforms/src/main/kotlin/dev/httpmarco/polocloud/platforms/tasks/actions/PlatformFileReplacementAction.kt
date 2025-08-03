package dev.httpmarco.polocloud.platforms.tasks.actions

import dev.httpmarco.polocloud.platforms.PlatformParameters
import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskStep
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

class PlatformFileReplacementAction() : PlatformAction() {

    override fun run(
        file: Path,
        step: PlatformTaskStep,
        environment: PlatformParameters
    ) {
        file.parent.createDirectories()
        file.createFile()
    }
}