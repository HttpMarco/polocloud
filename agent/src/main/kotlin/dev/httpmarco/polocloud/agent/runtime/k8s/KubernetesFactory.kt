package dev.httpmarco.polocloud.agent.runtime.k8s

import dev.httpmarco.polocloud.agent.runtime.RuntimeFactory
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot

class KubernetesFactory : RuntimeFactory<KubernetesService> {
    override fun bootApplication(service: KubernetesService) {
        TODO("Not yet implemented")
    }

    override fun shutdownApplication(service: KubernetesService, shutdownCleanUp: Boolean): ServiceSnapshot {
        TODO("Not yet implemented")
    }
}