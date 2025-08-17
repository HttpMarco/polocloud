package dev.httpmarco.polocloud.agent.information

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.docker.DockerRuntime
import dev.httpmarco.polocloud.agent.runtime.k8s.KubernetesRuntime
import dev.httpmarco.polocloud.common.os.cpuUsage
import dev.httpmarco.polocloud.common.os.maxMemory
import dev.httpmarco.polocloud.common.os.usedMemory
import dev.httpmarco.polocloud.shared.information.SharedCloudInformationProvider
import dev.httpmarco.polocloud.shared.information.CloudInformation

class CloudInformationStorageImpl : SharedCloudInformationProvider<CloudInformation> {

    override fun get(): CloudInformation {
        val runtime = Agent.runtime
        val runtimeString = if(runtime is KubernetesRuntime) "Kubernetes" else (if(runtime is DockerRuntime) "Docker" else "Local")
        return CloudInformation(
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