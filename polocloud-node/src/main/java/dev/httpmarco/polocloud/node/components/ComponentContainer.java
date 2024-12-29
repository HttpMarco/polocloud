package dev.httpmarco.polocloud.node.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.jar.JarFile;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ComponentContainer {

    private final Component component;
    private final ComponentInfoSnapshot snapshot;

    public ComponentContainer(JarFile jarFile) {
        // todo
        this.component = null;
        this.snapshot = null;
    }
}
