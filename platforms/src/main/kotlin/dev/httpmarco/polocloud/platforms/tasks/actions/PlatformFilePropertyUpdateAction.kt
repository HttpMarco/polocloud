package dev.httpmarco.polocloud.platforms.tasks.actions

import dev.httpmarco.polocloud.platforms.PlatformParameters
import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskStep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tomlj.Toml
import org.tomlj.TomlTable
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import org.yaml.snakeyaml.Yaml
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Path
import java.util.*
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

@Serializable
@SerialName("PlatformFilePropertyUpdateAction")
class PlatformFilePropertyUpdateAction(
    private val key: String,
    private val value: String
) : PlatformAction() {

    override fun run(file: Path, step: PlatformTaskStep, environment: PlatformParameters) {
        file.parent.createDirectories()
        val translatedValue = environment.modifyValueWithEnvironment(value)
        val parsedValue = parseValue(translatedValue)

        when {
            step.filename.endsWith(".properties") -> handleProperties(file, parsedValue.toString())
            step.filename.endsWith(".yml") -> handleYaml(file, parsedValue)
            step.filename.endsWith(".toml") -> handleToml(file, parsedValue)
        }
    }

    private fun handleProperties(file: Path, value: String) {
        val properties = Properties().apply {
            if (file.exists()) {
                FileInputStream(file.toFile()).use { load(it) }
            }
            setProperty(key, value)
        }

        FileOutputStream(file.toFile()).use {
            properties.store(it, "Updated configuration property: $key")
        }
    }

    private fun handleYaml(file: Path, value: Any?) {
        if (isNukkitConfig(file)) {
            handleSnakeYaml(file, value)
            return
        }

        handleConfigurateYaml(file, value)
    }

    private fun handleConfigurateYaml(file: Path, value: Any?) {
        val loader = YamlConfigurationLoader.builder().path(file).build()
        val root = if (file.exists()) loader.load() else loader.createNode()
        val path = key.split(".").map { it.toIntOrNull() ?: it }.toTypedArray()

        root.node(*path).set(value)
        loader.save(root)
    }

    private fun handleSnakeYaml(file: Path, value: Any?) {
        val yaml = Yaml()
        val data: MutableMap<String, Any?> = if (file.exists()) {
            FileReader(file.toFile()).use { yaml.load(it) as? MutableMap<String, Any?> } ?: mutableMapOf()
        } else {
            mutableMapOf()
        }

        data.withNestedValue(key.split("."), value)

        FileWriter(file.toFile()).use { yaml.dump(data, it) }
    }

    private fun handleToml(file: Path, value: Any?) {
        val fileText = if (file.exists()) file.toFile().readText() else ""
        val toml = Toml.parse(fileText)
        val existingMap = toml.toMap().toMutableMap()

        existingMap.withNestedValue(key.split("."), value)

        val newTomlContent = buildToml(existingMap)
        file.toFile().writeText(newTomlContent)
    }

    private fun parseValue(input: String): Any = when {
        input.equals("true", ignoreCase = true) -> true
        input.equals("false", ignoreCase = true) -> false
        input.toIntOrNull() != null -> input.toInt()
        else -> input
    }

    private fun buildToml(map: Map<String, Any?>, prefix: String = ""): String {
        val builder = StringBuilder()
        for ((key, value) in map) {
            val fullKey = if (prefix.isNotEmpty()) "$prefix.$key" else key
            when (value) {
                is Map<*, *> -> builder.append(buildToml(value as Map<String, Any?>, fullKey))
                is String -> builder.append("$fullKey = \"${value.replace("\"", "\\\"")}\"\n")
                else -> builder.append("$fullKey = $value\n")
            }
        }
        return builder.toString()
    }

    private fun MutableMap<String, Any?>.withNestedValue(keys: List<String>, value: Any?): MutableMap<String, Any?> {
        if (keys.isEmpty()) return this

        val key = keys.first()
        if (keys.size == 1) {
            this[key] = value
        } else {
            val child = when (val current = this[key]) {
                is TomlTable -> current.toMap().toMutableMap()
                is MutableMap<*, *> -> current as MutableMap<String, Any?>
                else -> mutableMapOf()
            }
            this[key] = child.withNestedValue(keys.drop(1), value)
        }
        return this
    }

    private fun isNukkitConfig(file: Path): Boolean {
        return file.fileName.toString().equals("nukkit.yml", ignoreCase = true)
    }
}