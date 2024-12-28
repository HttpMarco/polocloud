package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.node.sync.SyncId;
import dev.httpmarco.polocloud.node.sync.SyncTheme;

public final class ClusterGroupSyncTheme implements SyncTheme<ClusterGroup> {

    @Override
    public void delete(SyncId id, ClusterGroup object) {

    }

    @Override
    public void push(SyncId id, ClusterGroup object) {

    }
}
