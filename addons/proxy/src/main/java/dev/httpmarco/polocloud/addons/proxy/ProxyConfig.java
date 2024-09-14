package dev.httpmarco.polocloud.addons.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ProxyConfig {

    private final Motd[] motds;
    private final Motd[] maintenanceMotds;
    private final String maintenancePlayerInfo;
    private final Tablist tablist;

}
