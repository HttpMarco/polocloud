package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.services.Service
import dev.httpmarco.polocloud.agent.shutdownProcess
import dev.httpmarco.polocloud.platforms.PlatformType

class LocalRuntimeQueue : Thread("polocloud-local-runtime-queue") {


    override fun run() {
        try {
            while (!isInterrupted && !shutdownProcess()) {
                Agent.instance.runtime.groupStorage().items().forEach {
                    for (n in 0 until requiredServersThatStart(it)) {

                        val service = if(it.platform().type == PlatformType.PROXY) {
                            LocalService(it, findIndex(it), "0.0.0.0")
                        } else {
                            LocalService(it, findIndex(it))
                        }

                        Agent.instance.runtime.serviceStorage().deployService(service)
                        Agent.instance.runtime.factory().bootApplication(service)
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
        return (group.data.minOnlineService - group.serviceCount()).coerceAtLeast(0);
    }

    private fun findIndex(group: Group): Int {
        var id = 1
        while (Agent.instance.runtime.serviceStorage().items().stream()
                .anyMatch { it.group == group && it.id == id }
        ) {
            id++
        }
        return id
    }
}