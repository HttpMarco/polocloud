package dev.httpmarco.polocloud.node.components.tasks;

import dev.httpmarco.polocloud.node.components.ComponentContainer;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@UtilityClass
public final class ComponentUnloadTask {

    private static final Logger log = LogManager.getLogger(ComponentUnloadTask.class);

    @SneakyThrows
    public void unload(ComponentContainer container) {

        container.component().disabling();
        log.info("Unloading component {} ...", container.component());

        container.loader().close();
    }
}
