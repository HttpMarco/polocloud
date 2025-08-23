package dev.httpmarco.polocloud.agent.groups

import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.platforms.Platform
import dev.httpmarco.polocloud.platforms.PlatformPool
import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import java.nio.file.Path
import kotlin.io.path.Path
import dev.httpmarco.polocloud.agent.utils.IndexDetector
import dev.httpmarco.polocloud.agent.runtime.local.LocalService
import dev.httpmarco.polocloud.shared.groups.GroupInformation
import dev.httpmarco.polocloud.v1.GroupType

open class AbstractGroup(
    name: String,
    minMemory: Int,
    maxMemory: Int,
    minOnlineServices: Int,
    maxOnlineServices: Int,
    percentageToStartNewService: Double,
    platform: PlatformIndex,
    information: GroupInformation,
    templates: List<String>,
    properties: Map<String, JsonPrimitive>
) :
    Group(
        name,
        minMemory,
        maxMemory,
        minOnlineServices,
        maxOnlineServices,
        platform,
        percentageToStartNewService,
        information,
        templates,
        properties
    ) {

    fun update() {
        // update the group
        Agent.runtime.groupStorage().updateGroup(this)
    }

    fun serviceCount(): Int {
        return this.services().count()
    }

    fun platform(): Platform {
        return PlatformPool.find(platform.name)!!
    }

    fun services(): List<AbstractService> {
        return Agent.runtime.serviceStorage().findByGroup(this)
    }

    fun applicationPlatformFile(): Path {
        return Path("local/metadata/cache/${platform.name}/${platform.version}/${platform.name}-${platform.version}${platform().language.suffix()}")
    }

    fun shutdownAll() {
        Agent.runtime.serviceStorage().findByGroup(this).forEach { it.shutdown() }
    }

    fun playerCount(): Int {
        return services().sumOf { it.playerCount }
    }

    fun updateMinMemory(minMemory: Int) {
        this.minMemory = minMemory
    }

    fun updateMaxMemory(maxMemory: Int) {
        this.maxMemory = maxMemory
    }

    fun updateMinOnlineServices(minOnlineServices: Int) {
        this.minOnlineService = minOnlineServices
    }

    fun updateMaxOnlineServices(maxOnlineServices: Int) {
        this.maxOnlineService = maxOnlineServices
    }

    fun updatePercentageToStartNewService(percentageToStartNewService: Double) {
        this.percentageToStartNewService = percentageToStartNewService
    }

    fun startServices(amount: Int): List<AbstractService> {
        val startedServices = mutableListOf<AbstractService>()
        
        repeat(amount) {
            val index = IndexDetector.findIndex(this)
            val service = when (this.platform().type) {
                GroupType.PROXY -> LocalService(this, index, "0.0.0.0")
                else -> LocalService(this, index)
            }

            Agent.runtime.serviceStorage().deployAbstractService(service)
            Agent.runtime.factory().bootApplication(service)
            startedServices.add(service)
        }
        
        return startedServices
    }

    fun canStartServices(amount: Int): Boolean {
        val currentServices = this.serviceCount()
        val maxServices = this.maxOnlineService
        return maxServices == -1 || currentServices + amount <= maxServices
    }

    override fun equals(other: Any?): Boolean {
        return if (other is AbstractGroup) {
            this.name == other.name
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}