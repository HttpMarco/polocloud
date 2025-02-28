package dev.httpmarco.polocloud.suite.components;

import dev.httpmarco.polocloud.component.api.Component;
import dev.httpmarco.polocloud.suite.components.loader.ComponentClassLoader;

public record ComponentContainer(Component component, ComponentInfoSnapshot snapshot, ComponentClassLoader loader) {

}
