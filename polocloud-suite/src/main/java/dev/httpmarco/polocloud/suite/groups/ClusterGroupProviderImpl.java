package dev.httpmarco.polocloud.suite.groups;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.utils.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public final class ClusterGroupProviderImpl implements ClusterGroupProvider {


    private static final Logger log = LogManager.getLogger(ClusterGroupProviderImpl.class);

    public ClusterGroupProviderImpl() {
        log.info("Load 5 groups from local storage.");
        log.info("Synchronizing groups from head suite 'suite-3'.");
    }

    @Override
    public Future<Collection<ClusterGroup>> findAllAsync() {
        return null;
    }

    @Override
    public Future<ClusterGroup> findAsync(String groupId) {
        return null;
    }

    @Override
    public void delete(String group) {

    }

    @Override
    public ClusterGroupBuilder create(String group) {
        return null;
    }
}
