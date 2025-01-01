package dev.httpmarco.polocloud.node.dependencies;

import dev.httpmarco.polocloud.api.Available;
import dev.httpmarco.polocloud.api.Prepareable;
import dev.httpmarco.polocloud.node.dependencies.slots.ClassloaderDependencySlot;

import java.io.File;

public interface Dependency extends Available, Prepareable {

    File file();

    void initialize();

    ClassloaderDependencySlot boundSlot();

}
