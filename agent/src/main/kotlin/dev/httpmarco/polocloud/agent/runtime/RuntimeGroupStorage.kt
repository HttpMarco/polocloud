package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.utils.Reloadable
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider

interface RuntimeGroupStorage : SharedGroupProvider<AbstractGroup>, Reloadable {

    fun updateGroup(group: AbstractGroup)

    fun destroy(abstractGroup: AbstractGroup)

    fun publish(abstractGroup: AbstractGroup)

}