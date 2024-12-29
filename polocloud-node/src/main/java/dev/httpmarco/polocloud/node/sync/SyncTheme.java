package dev.httpmarco.polocloud.node.sync;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SyncTheme<T> {

    /**
     * Deletes an object from the sync
     * @param id the id
     * @param object the object
     */
    void delete(SyncId id, T object);

    /**
     * Pushes an object to the sync
     * @param id the id
     * @param object the object
     */
    void push(SyncId id, T object);

    /**
     * Grabs all objects from the sync
     * @param category the category
     * @return list of objects
     */
    List<T> grab(SyncCategory category);

    /**
     * Grabs one object from the sync
     * @param syncId the id
     * @return the object
     */
    @Nullable T grabOne(SyncId syncId);

}