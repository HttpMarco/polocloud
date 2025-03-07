package dev.httpmarco.polocloud.suite.cluster.global.syncstorage;

public interface SyncStorage<E> {

    void push(E data);

}
