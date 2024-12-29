package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.node.sync.SyncCategory;
import dev.httpmarco.polocloud.node.sync.SyncId;
import dev.httpmarco.polocloud.node.sync.SyncTheme;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ClusterGroupSyncTheme implements SyncTheme<ClusterGroup> {

    private final List<ClusterGroup> groups = new CopyOnWriteArrayList<>();

    @Override
    public void delete(SyncId id, ClusterGroup object) {
        this.groups.remove(object);
    }

    @Override
    public void push(SyncId id, ClusterGroup object) {
        this.groups.add(object);
    }

    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<ClusterGroup> grab(SyncCategory category) {
        return List.copyOf(groups);
    }

    @Override
    public ClusterGroup grabOne(SyncId syncId) {
        return this.groups.stream().filter(it -> it.name().equals(syncId.id())).findFirst().orElse(null);
    }
}
