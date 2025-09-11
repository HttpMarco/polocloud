package dev.httpmarco.polocloud.addons.api

import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.shared.service.Service

fun replacePlaceHolders(input: String, vararg objects: Any): String {
    var results = input
    objects.forEach {
        if(it is Group) {
            results = results.replace("%group%", it.name)
        }

        if(it is Service) {
            results = results.replace("%group%", it.groupName)
            results = results.replace("%service%", it.name())
            results = results.replace("%server%", it.name())
            results = results.replace("%motd%", it.motd)
            results = results.replace("%max_players%", it.maxPlayerCount.toString())
            results = results.replace("%online_players%", it.playerCount.toString())
            results = results.replace("%state%", it.state.name)
            results = results.replace("%type%", it.type.toString())
            results = results.replace("%id%", it.id.toString())
        }
    }
    return results
}