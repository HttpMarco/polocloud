package dev.httpmarco.polocloud.shared.platform

import dev.httpmarco.polocloud.v1.GroupType

interface SharedPlatformProvider<P : Platform> {

    /**
     * Finds and retrieves a list of available platform instances.
     *
     * @return A list of `Platform` objects representing the available platforms.
     */
    fun findAll(): List<P>

    /**
     * Finds a specific platform by its name.
     *
     * @param name The name of the platform to find.
     * @return The platform instance corresponding to the given name, or null if no platform is found with that name.
     */
    fun find(name: String): P?

    /**
     * Retrieves a list of platform instances that match the specified group type.
     *
     * @param type The group type used to filter the platform instances.
     * @return A list of platform instances corresponding to the specified group type.
     */
    fun find(type: GroupType): List<P>

}