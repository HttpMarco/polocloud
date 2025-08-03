package dev.httpmarco.polocloud.platforms.tasks.actions

import dev.httpmarco.polocloud.platforms.PlatformParameters
import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskStep
import java.nio.file.Path

class PlatformFileMoveAction(val oldPath: String, val newPath: String) : PlatformAction() {
    override fun run(
        file: Path,
        step: PlatformTaskStep,
        environment: PlatformParameters
    ) {
        file.resolve(environment.modifyValueWithEnvironment(oldPath)).toFile()
            .renameTo(
                file.resolve(environment.modifyValueWithEnvironment(newPath)).toFile()
            )
    }
}