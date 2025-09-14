package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.utils.Reloadable
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider

interface RuntimeGroupStorage : SharedGroupProvider<AbstractGroup>, Reloadable {

    override fun reload() {
        // Default implementation does nothing
    }
}