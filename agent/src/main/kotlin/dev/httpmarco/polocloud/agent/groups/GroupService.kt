package dev.httpmarco.polocloud.agent.groups

interface GroupService {

    fun find(name: String): Group?

    fun find(): List<Group>


}