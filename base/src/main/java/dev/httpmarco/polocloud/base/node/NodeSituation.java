package dev.httpmarco.polocloud.base.node;

public enum NodeSituation {

    INITIALIZE,
    // if new data from head node was reading
    SYNC,
    // ready and synced
    REACHABLE,
    // node was online, but now closed
    DISCONNECTED,
    // every time offline
    NOT_AVAILABLE

}
