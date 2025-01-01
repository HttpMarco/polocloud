package dev.httpmarco.polocloud.node.components.tasks;

import dev.httpmarco.polocloud.common.classpath.ModifiableClassloader;
import dev.httpmarco.polocloud.node.components.ComponentContainer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComponentSetupTask {

    public void setupTask() {



        var container = new ComponentContainer(null, null, new ModifiableClassloader());
    }

}
