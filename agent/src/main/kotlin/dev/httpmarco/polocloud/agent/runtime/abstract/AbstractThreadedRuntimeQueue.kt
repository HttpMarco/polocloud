package dev.httpmarco.polocloud.agent.runtime.abstract

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.shutdownProcess
import dev.httpmarco.polocloud.v1.services.ServiceState

open class AbstractThreadedRuntimeQueue : Thread("polocloud-local-runtime-queue") {

    override fun run() {
        try {
            val runtime = Agent.runtime
            while (!isInterrupted && !shutdownProcess()) {
                runtime.groupStorage().findAll().forEach { group ->
                    val required = requiredServersThatStart(group)

                    repeat(required) {
                        if (group.services().size >= group.maxOnlineService && group.maxOnlineService != -1) {
                            return@repeat
                        }

                        val service = Agent.runtime.factory().generateInstance(group)
                        runtime.serviceStorage().deployAbstractService(service)
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

    private fun requiredServersThatStart(group: AbstractGroup): Int {
        var minimumValue = (group.minOnlineService - group.serviceCount()).coerceAtLeast(0)

        val averageMaxPlayers =
            group.services().stream().filter { it.maxPlayerCount != -1 }.mapToInt { it.maxPlayerCount }.average()
        val averageOnlinePlayers =
            group.services().stream().filter { it.playerCount != -1 }.mapToInt { it.playerCount }.average()

        if (minimumValue <= 0 && averageMaxPlayers.isPresent && averageOnlinePlayers.isPresent && group.services()
                .none { it.state != ServiceState.ONLINE }
        ) {
            if (group.percentageToStartNewService < (100 / averageMaxPlayers.asDouble) * averageOnlinePlayers.asDouble) {
                minimumValue += 1
            }
        }
        return minimumValue
    }
}