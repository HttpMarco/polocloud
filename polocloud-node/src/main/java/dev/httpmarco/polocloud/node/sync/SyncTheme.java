package dev.httpmarco.polocloud.node.sync;

public interface SyncTheme<T> {

    void delete(SyncId id, T object);

    void push(SyncId id, T object);

}