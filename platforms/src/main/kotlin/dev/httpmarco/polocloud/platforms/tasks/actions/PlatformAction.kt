package dev.httpmarco.polocloud.platforms.tasks.actions

import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskStep
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import java.nio.file.Path
import javax.annotation.processing.RoundEnvironment

@Serializable
@Polymorphic
abstract class PlatformAction() {

    abstract fun run(file: Path, step: PlatformTaskStep, environment: Map<String, String>)

    protected fun modifyValueWithEnvironment(value: String, environment: Map<String, String>): String {
        var modifiedValue = value
        for ((envKey, envValue) in environment) {
            modifiedValue = modifiedValue.replace("%$envKey%", envValue)
        }
        return modifiedValue
    }

}