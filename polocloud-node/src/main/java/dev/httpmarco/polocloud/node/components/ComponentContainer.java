package dev.httpmarco.polocloud.node.components;

import dev.httpmarco.polocloud.common.classloader.ModifiableClassloader;

public record ComponentContainer(Component component, ComponentInfoSnapshot snapshot, ModifiableClassloader loader) {

}
