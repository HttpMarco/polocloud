package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.shutdownProcess

class LocalRuntimeQueue : Thread("polocloud-local-runtime-queue") {


    override fun run() {
        try {
            while (!isInterrupted && !shutdownProcess()) {
                Agent.instance.runtime.groupStorage().items().forEach {
                    for (n in 0 until requiredServersThatStart(it)) {
                        val service = LocalService(it, findIndex(it))
                        Agent.instance.runtime.serviceStorage().deployService(service)
                        Agent.instance.runtime.factory().bootApplication(service)
                    }
                }
                sleep(1000)
            }
        }catch (e : Throwable) {
            logger.throwable(e)
        }
    }

    private fun requiredServersThatStart(group : Group): Int {
        return (group.data.minOnlineService - group.serviceCount()).coerceAtLeast(0);
    }

    private fun findIndex(group: Group): Int {
        var id = 1
        while (Agent.instance.runtime.serviceStorage().items().stream()
                .anyMatch { it.group == group && it.id == id }) {
            id++
        }
        return id
    }
}