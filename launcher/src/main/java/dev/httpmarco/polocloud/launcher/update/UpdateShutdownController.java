package dev.httpmarco.polocloud.launcher.update;

import dev.httpmarco.polocloud.launcher.PoloCloudLauncher;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateShutdownController {

    @SneakyThrows
    public void shutdown() {
        Class.forName("dev.httpmarco.polocloud.node.NodeShutdown", false, PoloCloudLauncher.CLASS_LOADER).getMethod("nodeShutdown", boolean.class).invoke(null, false);
    }
}
