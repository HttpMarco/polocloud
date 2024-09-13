package dev.httpmarco.polocloud.plugin;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class PluginPermissions {

    // bypass if max players are reach
    public final String BYPASS_MAX_PLAYERS = "polocloud.bypass.max.players";

    // bypass if maintenance are enabled
    public final String BYPASS_MAINTENANCE = "polocloud.bypass.maintenance";
}
