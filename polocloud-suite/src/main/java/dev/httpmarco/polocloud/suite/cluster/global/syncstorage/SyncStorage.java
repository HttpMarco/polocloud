package dev.httpmarco.polocloud.suite.cluster.global.syncstorage;

import dev.httpmarco.polocloud.api.Available;

import java.util.List;

public interface SyncStorage<E> extends Available {

    void push(E data);

    List<E> entries();

    void update(E data);

    void delete(E data);

}
