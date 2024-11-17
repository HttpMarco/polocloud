package dev.httpmarco.polocloud.api.services;

public enum ClusterServiceFilter {

    // Collect all servers with zero players
    EMPTY_SERVICES,
    // Collect all servers with players present
    PLAYERS_PRESENT_SERVERS,
    // Find the fullest player server
    FULLEST_SERVER,
    // Find the emptiest player server
    EMPTIEST_SERVER,
    // Find all services on the same node
    SAME_NODE_SERVICES,
    // Collect all servers with the 'fallback' property
    FALLBACKS,
    // Find the emptiest server with the 'fallback' property
    EMPTIEST_FALLBACK,
    // All fallback servers ordered by player count
    SORTED_FALLBACKS,
    // Collection of all online services
    ONLINE_SERVERS,
    // Collect all proxies
    PROXIES,
    // Collect all services
    SERVICES,
    // Collect all servers
    SERVERS

}
