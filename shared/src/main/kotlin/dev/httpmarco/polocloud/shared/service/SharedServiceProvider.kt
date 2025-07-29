package dev.httpmarco.polocloud.shared.service

import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.v1.GroupType
import java.util.concurrent.CompletableFuture

/**
 * Interface for a shared service provider that allows interaction with services.
 */
interface SharedServiceProvider {

    /**
     * Finds all services.
     *
     * @return a list of all services
     */
    fun findAll() : List<Service>

    /**
     * Asynchronously finds all services.
     *
     * @return a CompletableFuture that will complete with a list of all services
     */
    fun findAllAsync() : CompletableFuture<List<Service>>

    /**
     * Finds a service by its name.
     *
     * @param name the name of the service
     * @return the service if found, null otherwise
     */
    fun find(name: String): Service?

    /**
     * Asynchronously finds a service by its name.
     *
     * @param name the name of the service
     * @return a CompletableFuture that will complete with the service if found, or null otherwise
     */
    fun findAsync(name: String): CompletableFuture<Service?>

    /**
     * Finds all services by their type.
     *
     * @param type the type of the services
     * @return a list of services belonging to the specified type
     */
    fun findByType(type: GroupType): List<Service>

    /**
     * Asynchronously finds all services by their type.
     *
     * @param type the type of the services
     * @return a CompletableFuture that will complete with a list of services belonging to the specified
     */
    fun findByTypeAsync(type: GroupType): CompletableFuture<List<Service>>

    /**
     * Finds all services by their group.
     *
     * @param group the group of the services
     * @return a list of services belonging to the specified group
     */
    fun findByGroup(group: Group) : List<Service>

    /**
     * Asynchronously finds all services by their group.
     *
     * @param group the group of the services
     * @return a CompletableFuture that will complete with a list of services belonging to the specified
     */
    fun findByGroupAsync(group: Group) : CompletableFuture<List<Service>>

    /**
     * Finds all services by their group name.
     *
     * @param group the name of the group
     * @return a list of services belonging to the specified group
     */
    fun findByGroup(group: String) : List<Service>

    /**
     * Asynchronously finds all services by their group name.
     *
     * @param group the name of the group
     * @return a CompletableFuture that will complete with a list of services belonging to the specified group
     */
    fun findByGroupAsync(group: String) : CompletableFuture<List<Service>>

    /**
     * Boots a service instance with the specified name and configuration.
     *
     * @param name the name of the service instance to boot
     * @param configuration a lambda that configures the boot process
     */
    fun bootInstanceWithConfiguration(name: String, configuration: (SharedBootConfiguration) -> Any)

    /**
     * Boots a service instance with the specified name.
     *
     * @param name the name of the service instance to boot
     */
    fun bootInstance(name: String)

}