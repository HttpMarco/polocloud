package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot

interface RuntimeFactory<out T : AbstractService> {

    fun bootApplication(service: @UnsafeVariance T)

    fun shutdownApplication(service: @UnsafeVariance T, shutdownCleanUp : Boolean = true): ServiceSnapshot

    fun generateInstance(group: AbstractGroup) : T

}