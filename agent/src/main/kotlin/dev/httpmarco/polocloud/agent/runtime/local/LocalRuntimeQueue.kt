package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.shutdownProcess
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceState

class LocalRuntimeQueue : Thread("polocloud-local-runtime-queue") {


    override fun run() {
        try {
            val runtime = Agent.runtime

            while (!isInterrupted && !shutdownProcess()) {
                runtime.groupStorage().items()
                    .forEach { group ->
                        val required = requiredServersThatStart(group)

                        repeat(required) {
                            if (group.services().size >= group.data.maxOnlineService) {
                                return@repeat
                            }
                            val index = findIndex(group)
                            val service = when (group.platform().type) {
                                GroupType.PROXY -> LocalService(group, index, "0.0.0.0")
                                else -> LocalService(group, index)
                            }

                            runtime.serviceStorage().deployService(service)
                            runtime.factory().bootApplication(service)
                        }
                    }
                sleep(1000)
            }
        } catch (_: InterruptedException) {
            // The Thread was interrupted, we can exit gracefully
        } catch (e: Throwable) {
            logger.throwable(e)
        }
    }

    private fun requiredServersThatStart(group: Group): Int {
        var minimumValue = (group.data.minOnlineService - group.serviceCount()).coerceAtLeast(0)

        val averageMaxPlayers = group.services().stream().filter { it.maxPlayerCount != -1 }.mapToInt { it.maxPlayerCount }.average()
        val averageOnlinePlayers = group.services().stream().filter { it.playerCount != -1 }.mapToInt { it.playerCount }.average()

        if (minimumValue <= 0 && averageMaxPlayers.isPresent && averageOnlinePlayers.isPresent && group.services().none { it -> it.state != ServiceState.ONLINE }) {
            if (group.data.percentageToStartNewService < (100 / averageMaxPlayers.asDouble) * averageOnlinePlayers.asDouble) {
                minimumValue += 1
            }
        }

        return minimumValue
    }

    private fun findIndex(group: Group): Int {
        var id = 1
        while (Agent.runtime.serviceStorage().items().stream()
                .anyMatch { it.group == group && it.id == id }
        ) {
            id++
        }
        return id
    }
}