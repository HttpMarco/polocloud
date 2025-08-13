package dev.httpmarco.polocloud.bridges.gate

import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path

class GateBridge(val servicePath: Path) : BridgeInstance<Server>() {
    override fun generateInfo(
        name: String,
        hostname: String,
        port: Int
    ): Server {
        return Server(name, hostname, port)
    }

    override fun registerService(identifier: Server, fallback: Boolean) {
        val loader = YamlConfigurationLoader.builder().path(servicePath.resolve("config.yml")).build()
        val config = loader.load()

        val serversNode = config.node("config", "servers")

        val serverAddress = "${identifier.hostname}:${identifier.port}"
        serversNode.node(identifier.name).set(serverAddress)

        if (fallback) {
            val tryNode = config.node("config", "try")
            val tryList = tryNode.childrenList().map { it.string }.toMutableList()

            if (!tryList.contains(identifier.name)) {
                tryList.add(identifier.name)
                tryNode.setList(String::class.java, tryList)
            }
        }

        loader.save(config)
    }

    override fun unregisterService(identifier: Server) {
        val loader = YamlConfigurationLoader.builder().path(servicePath.resolve("config.yml")).build()
        val config = loader.load()

        val serversNode = config.node("config", "servers")
        serversNode.removeChild(identifier.name)

        val tryNode = config.node("config", "try")
        val tryList = tryNode.childrenList().map { it.string }.toMutableList()
        tryList.remove(identifier.name)
        tryNode.setList(String::class.java, tryList)

        loader.save(config)
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
        } catch (e: Exception) {
            null
        }
    }
}