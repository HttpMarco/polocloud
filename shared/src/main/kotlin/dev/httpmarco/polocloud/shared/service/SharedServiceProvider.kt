package dev.httpmarco.polocloud.shared.service

import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import java.util.concurrent.CompletableFuture

/**
 * Interface for a shared service provider that allows interaction with services.
 */
interface SharedServiceProvider<S : Service> {

    /**
     * Finds all services.
     *
     * @return a list of all services
     */
    fun findAll(): List<S>

    /**
     * Asynchronously finds all services.
     *
     * @return a CompletableFuture that will complete with a list of all services
     */
    fun findAllAsync(): CompletableFuture<List<S>>

    /**
     * Finds a service by its name.
     *
     * @param name the name of the service
     * @return the service if found, null otherwise
     */
    fun find(name: String): S?

    /**
     * Asynchronously finds a service by its name.
     *
     * @param name the name of the service
     * @return a CompletableFuture that will complete with the service if found, or null otherwise
     */
    fun findAsync(name: String): CompletableFuture<S?>

    /**
     * Finds all services by their type.
     *
     * @param type the type of the services
     * @return a list of services belonging to the specified type
     */
    fun findByType(type: GroupType): List<S>

    /**
     * Asynchronously finds all services by their type.
     *
     * @param type the type of the services
     * @return a CompletableFuture that will complete with a list of services belonging to the specified
     */
    fun findByTypeAsync(type: GroupType): CompletableFuture<List<S>>

    /**
     * Finds all services by their group.
     *
     * @param group the group of the services
     * @return a list of services belonging to the specified group
     */
    fun findByGroup(group: Group): List<S>

    /**
     * Asynchronously finds all services by their group.
     *
     * @param group the group of the services
     * @return a CompletableFuture that will complete with a list of services belonging to the specified
     */
    fun findByGroupAsync(group: Group): CompletableFuture<List<S>>

    /**
     * Finds all services by their group name.
     *
     * @param group the name of the group
     * @return a list of services belonging to the specified group
     */
    fun findByGroup(group: String): List<S>

    /**
     * Asynchronously finds all services by their group name.
     *
     * @param group the name of the group
     * @return a CompletableFuture that will complete with a list of services belonging to the specified group
     */
    fun findByGroupAsync(group: String): CompletableFuture<List<S>>

    /**
     * Boots a service instance with the specified name and configuration.
     *
     * @param name the name of the service instance to boot
     * @param configuration a lambda that configures the boot process
     */
    fun bootInstanceWithConfiguration(name: String, configuration: SharedBootConfiguration): ServiceSnapshot

    /**
     * Boots a service instance with the specified name.
     *
     * @param name the name of the service instance to boot
     */
    fun bootInstance(name: String): ServiceSnapshot {
        return bootInstanceWithConfiguration(name, SharedBootConfiguration())
    }

    /**
     * Shuts down the specified service.
     */
    fun shutdownService(service: Service) = shutdownService(service.name())

    fun shutdownService(name: String): ServiceSnapshot

}