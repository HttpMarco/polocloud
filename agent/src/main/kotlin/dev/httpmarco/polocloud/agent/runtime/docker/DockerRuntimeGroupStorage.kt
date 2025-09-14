package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.ContainerSpec
import com.github.dockerjava.api.model.NetworkAttachmentConfig
import com.github.dockerjava.api.model.ServiceModeConfig
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions
import com.github.dockerjava.api.model.ServiceSpec
import com.github.dockerjava.api.model.TaskSpec
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.RuntimeGroupStorage
import dev.httpmarco.polocloud.common.language.Language
import dev.httpmarco.polocloud.common.os.currentOS
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import dev.httpmarco.polocloud.shared.properties.PropertyHolder
import dev.httpmarco.polocloud.shared.template.Template
import dev.httpmarco.polocloud.v1.services.ServiceState
import java.util.ArrayList
import java.util.concurrent.CompletableFuture
import kotlin.io.path.name

/**
 * A runtime storage implementation for managing AbstractGroups using Docker Swarm.
 * Each AbstractGroup is represented as a Docker Service.
 *
 * @param client The DockerClient used to interact with the Docker API.
 */
open class DockerRuntimeGroupStorage(private val client: DockerClient) : RuntimeGroupStorage {

    /**
     * Retrieves all groups stored in Docker.
     *
     * @return A list of all AbstractGroups.
     */
    override fun findAll(): List<AbstractGroup> {
        val services = client.listServicesCmd()
            .withLabelFilter(mapOf("polocloud" to "true"))
            .exec()
        return services.map { mapGroupData(it.spec?.labels ?: emptyMap()) }
    }

    /**
     * Retrieves all groups asynchronously.
     *
     * @return A CompletableFuture containing a list of all AbstractGroups.
     */
    override fun findAllAsync(): CompletableFuture<List<AbstractGroup>> =
        CompletableFuture.supplyAsync { findAll() }

    /**
     * Finds a group by its name.
     *
     * @param name The name of the group to find.
     * @return The AbstractGroup if found, null otherwise.
     */
    override fun find(name: String): AbstractGroup? =
        findServiceByName(name)?.let { mapGroupData(it.spec?.labels ?: emptyMap()) }

    /**
     * Finds a group by its name asynchronously.
     *
     * @param name The name of the group to find.
     * @return A CompletableFuture containing the AbstractGroup if found, null otherwise.
     */
    override fun findAsync(name: String): CompletableFuture<AbstractGroup?> =
        CompletableFuture.supplyAsync { find(name) }

    /**
     * Creates a Docker service for the given group.
     *
     * @param group The AbstractGroup to create.
     * @return The created AbstractGroup.
     */
    override fun create(group: AbstractGroup): AbstractGroup {
        client.createServiceCmd(buildServiceSpec(group)).exec()
        return group
    }

    /**
     * Creates a Docker service asynchronously.
     *
     * @param group The AbstractGroup to create.
     * @return A CompletableFuture containing the created AbstractGroup.
     */
    override fun createAsync(group: AbstractGroup): CompletableFuture<AbstractGroup?> =
        CompletableFuture.supplyAsync { create(group) }

    /**
     * Updates an existing Docker service to reflect the current state of the group.
     *
     * @param group The AbstractGroup with updated settings.
     * @return The updated AbstractGroup, or null if the service does not exist.
     */
    override fun update(group: AbstractGroup): AbstractGroup? {
        val service = findServiceByName(group.name) ?: return null
        client.updateServiceCmd(service.id, buildServiceSpec(group, service.spec?.name)).exec()
        return group
    }

    /**
     * Updates an existing Docker service asynchronously.
     *
     * @param group The AbstractGroup with updated settings.
     * @return A CompletableFuture containing the updated AbstractGroup, or null if not found.
     */
    override fun updateAsync(group: AbstractGroup): CompletableFuture<AbstractGroup?> =
        CompletableFuture.supplyAsync { update(group) }

    /**
     * Deletes a group by name and returns the deleted group.
     *
     * @param name The name of the group to delete.
     * @return The deleted AbstractGroup, or null if it did not exist.
     */
    override fun delete(name: String): AbstractGroup? {
        val group = find(name)
        group?.let {
            findServiceByName(group.name)?.let {
                client.removeServiceCmd(it.id).exec()
            }
        }
        return group
    }

    /**
     * Converts Docker service labels into an AbstractGroup object.
     *
     * @param data The labels map from a Docker service.
     * @return The AbstractGroup represented by the labels.
     */
    private fun mapGroupData(data: Map<String, String>): AbstractGroup =
        AbstractGroup(
            name = data["name"] ?: "null",
            minMemory = data["minMemory"]?.toInt() ?: -1,
            maxMemory = data["maxMemory"]?.toInt() ?: -1,
            minOnlineServices = data["minOnlineServices"]?.toInt() ?: -1,
            maxOnlineServices = data["maxOnlineServices"]?.toInt() ?: -1,
            percentageToStartNewService = data["percentageToStartNewService"]?.toDouble() ?: -1.0,
            platform = PlatformIndex(
                data["platformName"] ?: "null",
                data["platformVersion"] ?: "null"
            ),
            createdAt = data["createdAt"]?.toLong() ?: System.currentTimeMillis(),
            templates = data["templates"]?.split(",")?.map { Template(it) } ?: mutableListOf(),
            properties = PropertyHolder()
        )

    /**
     * Converts an AbstractGroup into Docker service labels.
     *
     * @param group The AbstractGroup to convert.
     * @return A map of label keys and values representing the group.
     */
    private fun toGroupData(group: AbstractGroup): Map<String, String> = mapOf(
        "polocloud" to "true",
        "name" to group.name,
        "state" to ServiceState.PREPARING.name,
        "minMemory" to group.minMemory.toString(),
        "maxMemory" to group.maxMemory.toString(),
        "minOnlineServices" to group.minOnlineService.toString(),
        "maxOnlineServices" to group.maxOnlineService.toString(),
        "percentageToStartNewService" to group.percentageToStartNewService.toString(),
        "type" to group.platform().type.name,
        "platformName" to group.platform.name,
        "platformVersion" to group.platform.version,
        "createdAt" to group.createdAt.toString(),
        "templates" to group.templates.joinToString(",") { it.name }
    )

    /**
     * Generates the correct startup command for the group based on its platform.
     *
     * @param group The AbstractGroup to generate command for.
     * @return A list of command arguments.
     */
    protected fun languageSpecificBootArguments(group: AbstractGroup): ArrayList<String> {
        val platform = group.platform()
        val commands = ArrayList<String>()

        when (platform.language) {
            Language.JAVA -> {
                commands.add(javaLanguagePath(group))
                commands.addAll(
                    listOf(
                        "-Dterminal.jline=false",
                        "-Dfile.encoding=UTF-8",
                        "-Xms${group.minMemory}M",
                        "-Xmx${group.maxMemory}M",
                        "-jar",
                        group.applicationPlatformFile().name
                    )
                )
                commands.addAll(platform.arguments)
            }

            Language.GO, Language.RUST -> {
                commands.addAll(currentOS.executableCurrentDirectoryCommand(group.applicationPlatformFile().name))
            }
        }

        return commands
    }

    /**
     * Returns the Java executable path. Can be overridden for custom paths.
     *
     * @param group The AbstractGroup to run.
     * @return The Java executable path as a string.
     */
    protected open fun javaLanguagePath(group: AbstractGroup): String = "java"

    /**
     * Builds a ServiceSpec for creating or updating a Docker service.
     *
     * @param group The AbstractGroup to represent.
     * @param serviceName Optional service name; defaults to "polocloud-{group.name}".
     * @return A ServiceSpec configured for Docker.
     */
    private fun buildServiceSpec(group: AbstractGroup, serviceName: String? = null): ServiceSpec =
        ServiceSpec()
            .withName(serviceName ?: "polocloud-${group.name}")
            .withLabels(toGroupData(group))
            .withTaskTemplate(
                TaskSpec().withContainerSpec(
                    ContainerSpec()
                        .withImage("openjdk:21-jdk")
                        .withDir("/app")
                        .withCommand(languageSpecificBootArguments(group))
                )
            )
            .withMode(
                ServiceModeConfig().withReplicated(
                    ServiceReplicatedModeOptions().withReplicas(group.minOnlineService)
                )
            )
            .withNetworks(
                listOf(NetworkAttachmentConfig().withTarget(DOCKER_NETWORK))
            )

    /**
     * Finds a Docker service by its group name.
     *
     * @param name The group name.
     * @return The Docker service if found, null otherwise.
     */
    private fun findServiceByName(name: String) =
        client.listServicesCmd()
            .withLabelFilter(mapOf("polocloud" to "true", "name" to name))
            .exec()
            .firstOrNull { it.spec?.name == "polocloud-$name" }
}
