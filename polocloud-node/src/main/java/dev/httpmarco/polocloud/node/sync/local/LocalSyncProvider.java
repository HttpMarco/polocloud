package dev.httpmarco.polocloud.node.sync.local;

import dev.httpmarco.polocloud.node.sync.SyncCategory;
import dev.httpmarco.polocloud.node.sync.SyncId;
import dev.httpmarco.polocloud.node.sync.SyncProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LocalSyncProvider implements SyncProvider {

    private final Map<SyncCategory, Map<SyncId, Object>> cache = new HashMap<>();

    @Override
    public void delete(@NotNull SyncId id) {
        if (!this.cache.containsKey(id.category())) {
            return;
        }
        this.cache.get(id.category()).remove(id);
    }

    @Override
    public void push(SyncId id, Object object) {
        this.cache.computeIfAbsent(id.category(), category -> new HashMap<>()).put(id, object);
    }

    @Override
    public List<Object> grab(SyncCategory category) {

        if(!this.cache.containsKey(category)) {
            return List.of();
        }

        return this.cache.get(category).values().stream().toList();
    }

    @Override
    public @Nullable Object grabOne(SyncId syncId) {
        return null;
    }
}
