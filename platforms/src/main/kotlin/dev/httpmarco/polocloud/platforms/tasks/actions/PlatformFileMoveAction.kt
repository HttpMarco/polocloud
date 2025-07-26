package dev.httpmarco.polocloud.platforms.tasks.actions

import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskStep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.nio.file.Path

@Serializable
@SerialName("PlatformFileMoveAction")
class PlatformFileMoveAction(val oldPath: String, val newPath: String) : PlatformAction() {
    override fun run(
        file: Path,
        step: PlatformTaskStep,
        environment: Map<String, String>
    ) {
        file.resolve(oldPath).toFile()
            .renameTo(
                file.resolve(newPath).toFile()
            )
    }
}