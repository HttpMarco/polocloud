package dev.httpmarco.polocloud.node.sync;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * represent the category of a sync objects
 * @param category the category
 */
public record SyncCategory(String category) {

    @Contract("_ -> new")
    public @NotNull SyncId idOf(String id) {
        return new SyncId(this, id);
    }
}
