package dev.httpmarco.polocloud.node.components;

import java.util.Collection;

/**
 * Accept all subcomponents for the cloud system
 * For example: rest component oder console component
 */
public interface ComponentProvider {

    Collection<Component> components();

}
