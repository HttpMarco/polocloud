package dev.httpmarco.polocloud.node.sync;

public record SyncCat(String category) {

    public SyncId idOf(String id) {
        return new SyncId(this, id);
    }
}
