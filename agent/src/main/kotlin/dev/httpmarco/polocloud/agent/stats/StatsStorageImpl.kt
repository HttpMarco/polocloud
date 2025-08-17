package dev.httpmarco.polocloud.agent.stats

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.docker.DockerRuntime
import dev.httpmarco.polocloud.agent.runtime.k8s.KubernetesRuntime
import dev.httpmarco.polocloud.common.os.cpuUsage
import dev.httpmarco.polocloud.common.os.maxMemory
import dev.httpmarco.polocloud.common.os.usedMemory
import dev.httpmarco.polocloud.shared.stats.SharedStatsProvider
import dev.httpmarco.polocloud.shared.stats.Stats

class StatsStorageImpl : SharedStatsProvider<Stats> {

    override fun get(): Stats {
        val runtime = Agent.runtime
        val runtimeString = if(runtime is KubernetesRuntime) "Kubernetes" else (if(runtime is DockerRuntime) "Docker" else "Local")
        return Stats(
            Agent.runtime.started(),
            runtimeString,
            System.getProperty("java.version"),
            cpuUsage(),
            usedMemory(),
            maxMemory(),
            Agent.eventService.registeredAmount()
        )
    }

}