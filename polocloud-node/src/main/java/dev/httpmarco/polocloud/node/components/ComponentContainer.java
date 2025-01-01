package dev.httpmarco.polocloud.node.components;

import dev.httpmarco.polocloud.common.classpath.ModifiableClassloader;

public record ComponentContainer(Component component, ComponentInfoSnapshot snapshot, ModifiableClassloader loader) {

}
