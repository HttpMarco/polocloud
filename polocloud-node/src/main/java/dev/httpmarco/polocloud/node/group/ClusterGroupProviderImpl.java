package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.protocol.CloudResultFuture;
import dev.httpmarco.polocloud.api.protocol.CloudResultInfo;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.group.impl.ClusterGroupBuilderImpl;
import dev.httpmarco.polocloud.node.group.storage.LocalFileStorageFactory;
import dev.httpmarco.polocloud.node.sync.SyncProvider;
import dev.httpmarco.polocloud.node.sync.SyncThemeItem;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Getter
@Accessors(fluent = true)
public final class ClusterGroupProviderImpl implements ClusterGroupProvider {

    private static final Logger log = LogManager.getLogger(ClusterGroupProviderImpl.class);
    private final ClusterGroupStorageFactory storageFactory = new LocalFileStorageFactory();
    private final SyncThemeItem<ClusterGroup> syncItem = new SyncThemeItem<>("groups");

    public ClusterGroupProviderImpl() {
        if (Node.instance().clusterProvider().runtimeHead()) {
            storageFactory.searchAll().forEach(group -> syncItem.push(group.name(), group));

            log.info("Loaded {} groups", syncItem.grab().size());
            return;
        }
        // sync groups and show different

    }

    @Contract(" -> new")
    @Override
    public @NotNull CloudResultFuture<Collection<ClusterGroup>> findAllAsync() {
        return CloudResultFuture.completedFuture(this.syncItem.grab());
    }

    @Contract("_ -> new")
    @Override
    public @NotNull CloudResultFuture<ClusterGroup> findAsync(String groupId) {
        return CloudResultFuture.completedFuture(this.syncItem.grabOne(groupId));
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
