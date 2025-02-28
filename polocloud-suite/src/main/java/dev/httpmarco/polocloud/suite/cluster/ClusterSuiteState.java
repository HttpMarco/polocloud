package dev.httpmarco.polocloud.suite.cluster;

public enum ClusterSuiteState {

    // boot process
    INITIALIZING,
    // if suite is ready for work
    AVAILABLE,
    // in boot process
    SYNCHRONIZATION,
    // if suite is closing
    CLOSING,
    // if suite is offline
    OFFLINE

}
