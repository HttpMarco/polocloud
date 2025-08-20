package dev.httpmarco.polocloud.bridges.gate

import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.ServiceChangePlayerCountEvent
import dev.httpmarco.polocloud.shared.service.Service
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Path

class GateBridge(val servicePath: Path, serviceName: String, agentPort: Int) : BridgeInstance<Server>() {
    private val polocloud: Polocloud = Polocloud(serviceName, agentPort, false)
    private val fallbackServices = ArrayList<Service>()
    private val yaml: Yaml

    init {
        // SnakeYAML konfigurieren
        val options = DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.isPrettyFlow = true
        yaml = Yaml(options)

        initialize(polocloud)
        subscribePlayerCount()
    }

    private fun subscribePlayerCount() {
        polocloud.eventProvider().subscribe(ServiceChangePlayerCountEvent::class.java, { event ->
            if (event.service.properties["fallback"].toBoolean()) {
                val oldService = fallbackServices.first { s -> event.service.name() == s.name() }
                fallbackServices.remove(oldService)
                fallbackServices.add(event.service)

                val configFile = servicePath.resolve("config.yml").toFile()
                val config = loadYamlConfig(configFile)
                updateFallback(config)
                saveYamlConfig(configFile, config)
            }
        })
    }

    private fun loadYamlConfig(configFile: java.io.File): MutableMap<String, Any> {
        return if (configFile.exists()) {
            FileReader(configFile).use { reader ->
                yaml.load(reader) as? MutableMap<String, Any> ?: mutableMapOf()
            }
        } else {
            mutableMapOf()
        }
    }

    private fun saveYamlConfig(configFile: java.io.File, config: MutableMap<String, Any>) {
        FileWriter(configFile).use { writer ->
            yaml.dump(config, writer)
        }
    }

    private fun sortServices() {
        fallbackServices.sortBy { it.playerCount }
    }

    override fun generateInfo(service: Service): Server {
        return Server(service.name(), service.hostname, service.port, service)
    }

    override fun registerService(identifier: Server, fallback: Boolean) {
        if (identifier.service != null) {
            fallbackServices.add(identifier.service)
        }

        val configFile = servicePath.resolve("config.yml").toFile()
        val config = loadYamlConfig(configFile)

        // Sicherstellen, dass die config-Struktur existiert
        val configSection = config.getOrPut("config") { mutableMapOf<String, Any>() } as MutableMap<String, Any>
        val serversSection =
            configSection.getOrPut<String, Any>("servers") { mutableMapOf<String, Any>() } as MutableMap<String, Any>

        val serverAddress = "${identifier.hostname}:${identifier.port}"
        serversSection[identifier.name] = serverAddress

        if (fallback) {
            updateFallback(config)
        }

        saveYamlConfig(configFile, config)
    }

    override fun unregisterService(identifier: Server) {
        val service = fallbackServices.first { it.name() == identifier.name }
        fallbackServices.remove(service)

        val configFile = servicePath.resolve("config.yml").toFile()
        val config = loadYamlConfig(configFile)

        val configSection = config["config"] as? MutableMap<String, Any>
        val serversSection = configSection?.get("servers") as? MutableMap<String, Any>
        serversSection?.remove(identifier.name)

        updateFallback(config)
        saveYamlConfig(configFile, config)
    }

    private fun updateFallback(config: MutableMap<String, Any>) {
        sortServices()

        val configSection = config.getOrPut("config") { mutableMapOf<String, Any>() } as MutableMap<String, Any>
        val orderedList = fallbackServices.map { it.name() }
        configSection["try"] = orderedList
    }

    override fun findInfo(name: String): Server? {
        return try {
            val configFile = servicePath.resolve("config.yml").toFile()
            val config = loadYamlConfig(configFile)

            val configSection = config["config"] as? Map<String, Any>
            val serversSection = configSection?.get("servers") as? Map<String, Any>
            val serverAddress = serversSection?.get(name) as? String

            if (serverAddress != null) {
                val parts = serverAddress.split(":")
                if (parts.size == 2) {
                    val hostname = parts[0]
                    val port = parts[1].toIntOrNull()
                    if (port != null) {
                        return Server(name, hostname, port)
                    }
                }
            }
            null
        } catch (_: Exception) {
            null
        }
    }
}
