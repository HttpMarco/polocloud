package dev.httpmarco.polocloud.node.sync;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SyncProvider {

    /**
     * Deletes an object from the sync
     * @param id the id
     */
    void delete(SyncId id);

    /**
     * Pushes an object to the sync
     * @param id the id
     * @param object the object
     */
    void push(SyncId id, Object object);

    /**
     * Grabs all objects from the sync
     * @param category the category
     * @return list of objects
     */
    List<Object> grab(SyncCategory category);

    /**
     * Grabs one object from the sync
     * @param syncId the id
     * @return the object
     */
    @Nullable Object grabOne(SyncId syncId);

}