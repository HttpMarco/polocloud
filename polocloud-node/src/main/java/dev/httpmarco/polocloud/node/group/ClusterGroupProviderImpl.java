package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.protocol.CloudResultFuture;
import dev.httpmarco.polocloud.api.protocol.CloudResultInfo;
import dev.httpmarco.polocloud.node.group.impl.ClusterGroupBuilderImpl;
import dev.httpmarco.polocloud.node.group.storage.LocalFileStorageFactory;
import dev.httpmarco.polocloud.node.sync.SyncCategory;
import dev.httpmarco.polocloud.node.sync.SyncTheme;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Getter
@Accessors(fluent = true)
public final class ClusterGroupProviderImpl implements ClusterGroupProvider {

    private final ClusterGroupStorageFactory storageFactory = new LocalFileStorageFactory();
    private final SyncCategory groupSyncCategory = new SyncCategory("groups");
    private final SyncTheme<ClusterGroup> syncTheme;

    public ClusterGroupProviderImpl() {
        this.syncTheme = new ClusterGroupSyncTheme();
    }

    @Contract(" -> new")
    @Override
    public @NotNull CloudResultFuture<Collection<ClusterGroup>> findAllAsync() {
        return CloudResultFuture.completedFuture(this.syncTheme.grab(groupSyncCategory));
    }

    @Contract("_ -> new")
    @Override
    public @NotNull CloudResultFuture<ClusterGroup> findAsync(String groupId) {
        return CloudResultFuture.completedFuture(this.syncTheme.grabOne(groupSyncCategory.idOf(groupId)));
    }


    @Override
    public CloudResultInfo delete(String group) {
        return null;
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ClusterGroupBuilder create(String group) {
        return new ClusterGroupBuilderImpl(group, this);
    }
}
