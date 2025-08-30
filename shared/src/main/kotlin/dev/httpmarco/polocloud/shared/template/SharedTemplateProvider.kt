package dev.httpmarco.polocloud.shared.template

import java.util.concurrent.CompletableFuture

/**
 * Interface for a shared template provider that allows interaction with templates.
 */
interface SharedTemplateProvider<T : Template> {

    /**
     * Finds all templates.
     *
     * @return a list of all templates
     */
    fun findAll(): List<T>

    /**
     * Asynchronously finds all templates.
     *
     * @return a CompletableFuture that will complete with a list of all templates
     */
    fun findAllAsync(): CompletableFuture<List<T>>

    /**
     * Finds a template by its name.
     *
     * @param name the name of the template
     * @return the template if found, null otherwise
     */
    fun find(name: String): T?

    /**
     * Asynchronously finds a template by its name.
     *
     * @param name the name of the template
     * @return a CompletableFuture that will complete with the service if found, or null otherwise
     */
    fun findAsync(name: String): CompletableFuture<T?>

}