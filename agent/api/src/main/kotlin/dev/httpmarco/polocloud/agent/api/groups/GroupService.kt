package dev.httpmarco.polocloud.agent.api.groups

interface GroupService {

    fun find(name: String): Group?

    fun find(): List<Group>

}