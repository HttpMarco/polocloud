package dev.httpmarco.polocloud.agent.utils

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.v1.GroupType
import java.net.InetSocketAddress
import java.net.ServerSocket

class PortDetector {

    companion object {
        fun nextPort(abstractGroup: AbstractGroup): Int {
            var port = if (abstractGroup.platform().type == GroupType.PROXY) 25565 else 30000

            val startPortProperty = abstractGroup.properties["startPort"]
            if (startPortProperty != null) {
                try {
                    port = startPortProperty.asInt
                } catch (e: NumberFormatException) {
                    i18n.error("agent.utils.port-detector.port.cast.error", startPortProperty, abstractGroup.name)
                }
            }

            while (isPortUsed(port)) {
                port += 2
            }
            return port
        }

        private fun isPortUsed(port: Int): Boolean {
            for (service in Agent.runtime.serviceStorage().findAll()) {
                if (service.port == port) {
                    return true
                }
            }
            try {
                ServerSocket().use { serverSocket ->
                    serverSocket.bind(InetSocketAddress(port))
                    return false
                }
            } catch (_: Exception) {
                return true
            }
        }
    }
}