package dev.httpmarco.polocloud.platforms.tasks

import dev.httpmarco.polocloud.platforms.tasks.actions.PlatformAction
import kotlinx.serialization.Serializable
import java.nio.file.Path
import javax.annotation.processing.RoundEnvironment

@Serializable
class PlatformTaskStep(val name: String, val description: String, val filename: String, val action: PlatformAction) {

    fun run(servicePath: Path, environment: Map<String, String>) {
        this.action.run(servicePath.resolve(filename), this, environment)
    }

}