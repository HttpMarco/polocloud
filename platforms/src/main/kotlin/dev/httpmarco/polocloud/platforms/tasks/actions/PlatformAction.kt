package dev.httpmarco.polocloud.platforms.tasks.actions

import dev.httpmarco.polocloud.platforms.PlatformParameters
import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskStep
import java.nio.file.Path

abstract class PlatformAction() {

    abstract fun run(file: Path, step: PlatformTaskStep, environment: PlatformParameters)


}