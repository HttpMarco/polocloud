package dev.httpmarco.polocloud.node.sync;

/**
 * represent the category of a sync objects
 * @param category the category
 */
public record SyncCat(String category) {

    public SyncId idOf(String id) {
        return new SyncId(this, id);
    }
}
