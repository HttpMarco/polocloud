package dev.httpmarco.polocloud.suite.cluster.storage;

import java.util.List;

public interface ClusterStorage<I, T> {

    /**
     * Initialize the storage.
     */
    void initialize();

    /**
     * Get a list of all storage items.
     * @return all items
     */
    List<T> items();

    /**
     * Update the storage with the given item.
     * @param item the item to update
     */
    void publish(T item);

    /**
     * Get a single group by its identifier.
     * @param identifier the identifier of the group
     * @return the group
     */
    T singleton(I identifier);

    /**
     * Destroy a group by its identifier.
     * @param identifier the identifier of the item
     */
    void destroy(I identifier);

    /**
     * Extract the identifier from the item.
     * @param item the item to extract the identifier from
     * @return the identifier
     */
    I extreactIdentifier(T item);

}
