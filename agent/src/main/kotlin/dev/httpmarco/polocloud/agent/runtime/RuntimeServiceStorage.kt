package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.shared.service.SharedServiceProvider

interface RuntimeServiceStorage<S : AbstractService> : SharedServiceProvider<S> {

    fun deployService(service: S)

    fun deployAbstractService(abstractService: AbstractService) {
        deployService(implementedService(abstractService))
    }

    fun dropService(service: S)

    fun dropAbstractService(abstractService: AbstractService) {
        dropService(implementedService(abstractService))
    }

    fun implementedService(abstractService: AbstractService) : S

}