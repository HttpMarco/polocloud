package dev.httpmarco.polocloud.node.dependencies;

import dev.httpmarco.polocloud.api.Prepareable;

public interface DependencySlot extends Prepareable {

    void rawBind(Dependency file);

}
