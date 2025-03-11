package dev.httpmarco.polocloud.suite.cluster.global.syncstorage;

import java.util.List;

public interface SyncStorage<E> {

    void push(E data);

    List<E> entries();

    void update(E data);

}
