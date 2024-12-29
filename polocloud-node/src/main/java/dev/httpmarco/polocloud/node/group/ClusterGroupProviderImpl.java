package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.protocol.CloudResultFuture;
import dev.httpmarco.polocloud.node.sync.SyncCategory;
import dev.httpmarco.polocloud.node.sync.SyncTheme;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class ClusterGroupProviderImpl implements ClusterGroupProvider {

    private final SyncCategory groupSyncCategory = new SyncCategory("groups");
    private final SyncTheme<ClusterGroup> syncTheme;

    public ClusterGroupProviderImpl() {
        this.syncTheme = new ClusterGroupSyncTheme();
    }

    @Override
    public CloudResultFuture<Collection<ClusterGroup>> findAllAsync() {
        return CloudResultFuture.completedFuture(this.syncTheme.grab(groupSyncCategory));
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
