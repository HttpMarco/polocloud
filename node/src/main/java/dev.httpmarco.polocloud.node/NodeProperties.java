package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.api.properties.Property;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class NodeProperties {

    // define the start port of all proxies on this node. Default: 25565
    public static final Property<Integer> PROXY_PORT_START_RANGE = Property.ofInteger("PROXY_PORT_START_RANGE");

    // define the start port of all servers (spigot, paper...) on this node. Default: 30000
    public static final Property<Integer> SERVER_PORT_START_RANGE = Property.ofInteger("SERVER_PORT_START_RANGE");

    // define the start port of all services (multi-paper master) on this node. Default: 25565
    public static final Property<Integer> SERVICE_PORT_START_RANGE = Property.ofInteger("SERVICE_PORT_START_RANGE");

    // if a player connect the cluster with a proxy, we notify this in the terminal
    public static final Property<Boolean> LOG_PLAYERS_CONNECTION = Property.ofBoolean("LOG_PLAYERS_CONNECTION");

    // if a player disconnect the cluster with a proxy, we notify this in the terminal
    public static final Property<Boolean> LOG_PLAYERS_DISCONNECTION = Property.ofBoolean("LOG_PLAYERS_DISCONNECTION");

    // if a player switch the server, we notify this in the terminal
    public static final Property<Boolean> LOG_PLAYERS_SERVER_SWITCH = Property.ofBoolean("LOG_PLAYERS_SERVER_SWITCH");

}
