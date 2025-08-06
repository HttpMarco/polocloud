package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot

interface RuntimeFactory<out T : AbstractService> {

    fun bootApplication(service: @UnsafeVariance T)

    fun shutdownApplication(service: @UnsafeVariance T, shutdownCleanUp : Boolean = true): ServiceSnapshot

}