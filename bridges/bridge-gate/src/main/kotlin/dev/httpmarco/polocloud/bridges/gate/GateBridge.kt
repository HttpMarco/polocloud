package dev.httpmarco.polocloud.bridges.gate

import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.service.ServiceChangePlayerCountEvent
import dev.httpmarco.polocloud.shared.service.Service
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Path

class GateBridge(val servicePath: Path, serviceName: String, agentPort: Int, agentHostname: String) :
    BridgeInstance<Server, Server>(Polocloud(serviceName, agentHostname, agentPort, false)) {
    private val yaml: Yaml

    init {
        this.processBind()

        // SnakeYAML konfigurieren
        val options = DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.isPrettyFlow = true
        yaml = Yaml(options)
        subscribePlayerCount()
    }

    private fun subscribePlayerCount() {
        polocloud.eventProvider().subscribe(ServiceChangePlayerCountEvent::class.java, { event ->
            if (event.service.properties["fallback"].toBoolean()) {
                val oldService = registeredFallbacks.remove(event.service)
                registeredFallbacks[event.service] = oldService!!

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

    private fun updateFallback(config: MutableMap<String, Any>) {
        val configSection = config.getOrPut("config") { mutableMapOf<String, Any>() } as MutableMap<String, Any>
        val orderedList = registeredFallbacks.toList().stream().sorted(Comparator.comparing { playerCount(it.second) })
            .map { it.first.name() }.toList()
        configSection["try"] = orderedList
    }

    override fun generateServerInfo(service: Service): Server {
        return Server(service.name(), service.hostname, service.port, service)
    }

    override fun registerServerInfo(identifier: Server, service: Service): Server {
        val configFile = servicePath.resolve("config.yml").toFile()
        val config = loadYamlConfig(configFile)

        // Sicherstellen, dass die config-Struktur existiert
        val configSection = config.getOrPut("config") { mutableMapOf<String, Any>() } as MutableMap<String, Any>
        val serversSection =
            configSection.getOrPut("servers") { mutableMapOf<String, Any>() } as MutableMap<String, Any>

        val serverAddress = "${identifier.hostname}:${identifier.port}"
        serversSection[identifier.name] = serverAddress

        saveYamlConfig(configFile, config)
        return findServer(identifier.name)!!
    }

    override fun registerNewServer(service: Service) {
        super.registerNewServer(service)

        val configFile = servicePath.resolve("config.yml").toFile()
        val config = loadYamlConfig(configFile)

        updateFallback(config)

        saveYamlConfig(configFile, config)
   }

    override fun unregister(identifier: Server) {
        val configFile = servicePath.resolve("config.yml").toFile()
        val config = loadYamlConfig(configFile)

        val configSection = config["config"] as? MutableMap<*, *>
        val serversSection = configSection?.get("servers") as? MutableMap<*, *>
        serversSection?.remove(identifier.name)

        updateFallback(config)
        saveYamlConfig(configFile, config)
    }

    override fun findServer(name: String): Server? {
        return try {
            val configFile = servicePath.resolve("config.yml").toFile()
            val config = loadYamlConfig(configFile)

            val configSection = config["config"] as? Map<*, *>
            val serversSection = configSection?.get("servers") as? Map<*, *>
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

    override fun playerCount(info: Server): Int {
        return info.service!!.playerCount
    }
}
