package dev.httpmarco.polocloud.platforms.tasks.actions

import com.electronwill.nightconfig.core.file.FileConfig
import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskStep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists

@Serializable
@SerialName("PlatformFilePropertyUpdateAction")
class PlatformFilePropertyUpdateAction(private val key: String, private val value: String) : PlatformAction() {

    override fun run(
        file: Path,
        step: PlatformTaskStep,
        environment: Map<String, String>
    ) {
        if (step.filename.endsWith(".properties")) {
            val properties = Properties()
            if (file.exists()) {
                FileInputStream(file.toFile()).use { input -> properties.load(input) }
            }

            properties.setProperty(key, modifyValueWithEnvironment(value, environment))

            FileOutputStream(file.toFile()).use { output ->
                properties.store(
                    output,
                    "Update configuration property: $key"
                )
            }
        } else if(step.filename.endsWith(".yml")) {
            val config = FileConfig.of(file)
            config.set<String>(key, value)
            config.save()
        }
    }
}