package dev.httpmarco.polocloud.base.node;

public enum NodeSituation {

    INITIALIZE,
    // search for endpoint state
    DETECT_SIT,
    // if new data from head node was reading
    SYNC,
    // ready and synced
    REACHABLE,
    // node was online, but now closed
    DISCONNECTED,
    // every time offline
    NOT_AVAILABLE

}
