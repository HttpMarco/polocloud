package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.ContainerSpec
import com.github.dockerjava.api.model.ServiceModeConfig
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions
import com.github.dockerjava.api.model.ServiceSpec
import com.github.dockerjava.api.model.TaskSpec
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.RuntimeGroupStorage
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import dev.httpmarco.polocloud.shared.properties.PropertyHolder
import dev.httpmarco.polocloud.shared.template.Template
import java.util.concurrent.CompletableFuture

class DockerRuntimeGroupStorage(val client: DockerClient) : RuntimeGroupStorage {

    override fun updateGroup(group: AbstractGroup) {
        TODO("Not yet implemented")
    }

    override fun destroy(abstractGroup: AbstractGroup) {
        val services = client.listServicesCmd()
            .withLabelFilter(mapOf("polocloud" to "true", "name" to abstractGroup.name))
            .exec()
            .filter { it.spec?.name == "polocloud-${abstractGroup.name}" }

        if (services.isEmpty()) {
            return
        }

        services.forEach { service ->
            client.removeServiceCmd(service.id).exec()
        }
    }

    override fun publish(abstractGroup: AbstractGroup) {
       this.create(group = abstractGroup)
    }

    override fun findAll(): List<AbstractGroup> {
        val services = client.listServicesCmd()
            .withLabelFilter(mapOf("polocloud" to "true"))
            .exec()

        return services.map { mapGroupData(it.spec?.labels ?: emptyMap()) }
    }

    override fun findAllAsync(): CompletableFuture<List<AbstractGroup>> = CompletableFuture.supplyAsync { findAll() }

    override fun find(name: String): AbstractGroup? {
        val services = client.listServicesCmd()
            .withLabelFilter(mapOf("polocloud" to "true", "name" to name))
            .exec()
            .filter { it.spec?.name == "polocloud-$name" }

        if (services.isEmpty()) {
            return null
        }

        val service = services.first()
        return mapGroupData(service.spec?.labels ?: emptyMap())
    }

    override fun findAsync(name: String): CompletableFuture<AbstractGroup?> {
        return CompletableFuture.supplyAsync { find(name) }
    }

    override fun create(group: AbstractGroup): AbstractGroup {
        val serviceSpec = ServiceSpec()
            .withName("polocloud-${group.name}")
            .withLabels(toGroupData(group))
            .withTaskTemplate(TaskSpec().withContainerSpec(ContainerSpec().withImage("open-jdk:jdk17")))
            .withMode(ServiceModeConfig().withReplicated(ServiceReplicatedModeOptions().withReplicas(group.minOnlineService)))

        client.createServiceCmd(serviceSpec).exec()
        return group
    }

    override fun createAsync(group: AbstractGroup): CompletableFuture<AbstractGroup?> {
        return CompletableFuture.supplyAsync { create(group) }
    }

    override fun update(group: AbstractGroup): AbstractGroup? {
        TODO("Not yet implemented")
    }

    override fun updateAsync(group: AbstractGroup): CompletableFuture<AbstractGroup?> {
        return CompletableFuture.supplyAsync { update(group) }
    }

    override fun delete(name: String): AbstractGroup? {
        TODO("Not yet implemented")
    }

    override fun reload() {
        TODO("Not yet implemented")
    }

    private fun mapGroupData(data: Map<String, String>): AbstractGroup {
        return AbstractGroup(
            name = data["name"] ?: "null",
            minMemory = data["minMemory"]?.toInt() ?: -1,
            maxMemory = data["maxMemory"]?.toInt() ?: -1,
            minOnlineServices = data["minOnlineServices"]?.toInt() ?: -1,
            maxOnlineServices = data["maxOnlineServices"]?.toInt() ?: -1,
            percentageToStartNewService = data["percentageToStartNewService"]?.toDouble() ?: -1.0,
            platform = PlatformIndex(data["platformName"] ?: "null", data["platformVersion"] ?: "null"),
            createdAt = data["createdAt"]?.toLong() ?: System.currentTimeMillis(),
            templates = data["templates"]?.split(",")?.map { Template(it) } ?: mutableListOf(),
            properties = PropertyHolder()
        )
    }

    private fun toGroupData(group: AbstractGroup): Map<String, String> {
        val data = mutableMapOf<String, String>()
        data["polocloud"] = "true"
        data["name"] = group.name
        data["minMemory"] = group.minMemory.toString()
        data["maxMemory"] = group.maxMemory.toString()
        data["minOnlineServices"] = group.minOnlineService.toString()
        data["maxOnlineServices"] = group.maxOnlineService.toString()
        data["percentageToStartNewService"] = group.percentageToStartNewService.toString()
        data["platformName"] = group.platform.name
        data["platformVersion"] = group.platform.version
        data["createdAt"] = group.createdAt.toString()
        data["templates"] = group.templates.joinToString(",") { it.name }
        return data
    }
}