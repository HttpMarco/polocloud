package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.protocol.CloudResultFuture;
import dev.httpmarco.polocloud.node.sync.SyncTheme;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;

public final class ClusterGroupProviderImpl implements ClusterGroupProvider {

    private final @Nullable SyncTheme<ClusterGroup> syncTheme;

    public ClusterGroupProviderImpl() {
        this.syncTheme = new ClusterGroupSyncTheme();
    }

    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable Collection<ClusterGroup> findAll() {
        return List.of();
    }

    @Override
    public CloudResultFuture<Collection<ClusterGroup>> findAllAsync() {
        return null;
    }

    @Override
    public @Nullable ClusterGroup find(String groupId) {
        return null;
    }

    @Override
    public @Nullable ClusterGroup findAsync(String groupId) {
        return null;
    }

    @Override
    public ClusterGroupBuilder create(String group) {
        return null;
    }
}
