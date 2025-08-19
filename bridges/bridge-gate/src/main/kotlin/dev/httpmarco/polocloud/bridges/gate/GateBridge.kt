package dev.httpmarco.polocloud.bridges.gate

import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.ServiceChangePlayerCountEvent
import dev.httpmarco.polocloud.shared.service.Service
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path

class GateBridge(val servicePath: Path, serviceName: String, agentPort: Int) : BridgeInstance<Server>() {
    private val polocloud: Polocloud = Polocloud(serviceName, agentPort, false)
    private val fallbackServices = ArrayList<Service>()

    init {
        initialize(polocloud)
        subscribePlayerCount()
    }

    private fun subscribePlayerCount() {
        polocloud.eventProvider().subscribe(ServiceChangePlayerCountEvent::class.java, { event ->
            if (event.service.properties["fallback"].toBoolean()) {
                val oldService = fallbackServices.first { s -> event.service.name() == s.name() }
                fallbackServices.remove(oldService)
                fallbackServices.add(event.service)


                val loader = YamlConfigurationLoader.builder().path(servicePath.resolve("config.yml")).build()
                val config = loader.load()
                updateFallback(config)
                loader.save(config)
            }
        })
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

        val loader = YamlConfigurationLoader.builder().path(servicePath.resolve("config.yml")).build()
        val config = loader.load()

        val serversNode = config.node("config", "servers")

        val serverAddress = "${identifier.hostname}:${identifier.port}"
        serversNode.node(identifier.name).set(serverAddress)

        if (fallback) {
            updateFallback(config)
        }

        loader.save(config)
    }

    override fun unregisterService(identifier: Server) {
        val service = fallbackServices.first { it.name() == identifier.name }
        fallbackServices.remove(service)

        val loader = YamlConfigurationLoader.builder().path(servicePath.resolve("config.yml")).build()
        val config = loader.load()

        val serversNode = config.node("config", "servers")
        serversNode.removeChild(identifier.name)

        updateFallback(config)

        loader.save(config)
    }

    private fun updateFallback(config: CommentedConfigurationNode) {
        sortServices()

        val tryNode = config.node("config", "try")
        val orderedList = fallbackServices.map { it.name() }

        tryNode.setList(String::class.java, orderedList)
    }

    override fun findInfo(name: String): Server? {
        return try {
            val loader = YamlConfigurationLoader.builder().path(servicePath.resolve("config.yml")).build()
            val config = loader.load()

            val serverAddress = config.node("config", "servers", name).string

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
