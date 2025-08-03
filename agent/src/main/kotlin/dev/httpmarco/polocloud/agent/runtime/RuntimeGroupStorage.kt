package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider

interface RuntimeGroupStorage : SharedGroupProvider<AbstractGroup>{

    fun update(group: AbstractGroup)

    fun reload()

    fun destroy(abstractGroup: AbstractGroup)

    fun publish(abstractGroup: AbstractGroup)

}