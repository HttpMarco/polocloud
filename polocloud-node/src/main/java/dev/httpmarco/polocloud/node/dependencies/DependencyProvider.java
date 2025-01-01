package dev.httpmarco.polocloud.node.dependencies;

import dev.httpmarco.polocloud.node.dependencies.slots.ClassloaderDependencySlot;

import java.util.List;

public interface DependencyProvider {

    ClassloaderDependencySlot originalSlot();

    List<ClassloaderDependencySlot> slots();

    void loadDefaults();

}
