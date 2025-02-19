package dev.httpmarco.polocloud.suite.components;

import dev.httpmarco.polocloud.api.Closeable;

import java.util.Collection;

/**
 * Accept all subcomponents for the cloud system
 * For example: rest component or console component
 */
public interface ComponentProvider extends Closeable {

    Collection<ComponentContainer> containers();

    /**
     * Find a component by its snapshot
     * @param snapshot the snapshot of the component
     * @return the component container
     */
    ComponentContainer find(ComponentInfoSnapshot snapshot);

    /**
     * Find a component by its name
     * @param name the name of the component
     * @return the component container
     */
    ComponentContainer find(String name);

}