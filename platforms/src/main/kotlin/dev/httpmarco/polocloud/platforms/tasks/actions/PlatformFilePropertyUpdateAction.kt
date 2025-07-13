package dev.httpmarco.polocloud.platforms.tasks.actions

import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskStep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.*
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

@Serializable
@SerialName("PlatformFilePropertyUpdateAction")
class PlatformFilePropertyUpdateAction(private val key: String, private val value: String) : PlatformAction() {

    override fun run(
        file: Path,
        step: PlatformTaskStep,
        environment: Map<String, String>
    ) {
        file.parent.createDirectories()

        val translatedValue = modifyValueWithEnvironment(value, environment)

        if (step.filename.endsWith(".properties")) {
            val properties = Properties()
            if (file.exists()) {
                FileInputStream(file.toFile()).use { input -> properties.load(input) }
            }

            properties.setProperty(key, modifyValueWithEnvironment(translatedValue, environment))

            FileOutputStream(file.toFile()).use { output ->
                properties.store(
                    output,
                    "Update configuration property: $key"
                )
            }
        } else if (step.filename.endsWith(".yml")) {
            val loader = YamlConfigurationLoader.builder()
                .path(file)
                .build()

            val root = if (file.exists()) loader.load() else loader.createNode()
            val path = key.split(".")

            if (translatedValue == "true" || translatedValue == "false") {
                // Convert boolean strings to actual booleans
                root.node(*path.toTypedArray()).set(translatedValue.toBoolean())
            } else if (translatedValue.toIntOrNull() != null) {
                // Convert numeric strings to actual integers
                root.node(*path.toTypedArray()).set(translatedValue.toInt())
            } else {
                // Otherwise, treat it as a string
                root.node(*path.toTypedArray()).set(translatedValue)
            }
            loader.save(root)
        }
    }
}