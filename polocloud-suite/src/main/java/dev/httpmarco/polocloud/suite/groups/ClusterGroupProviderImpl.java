package dev.httpmarco.polocloud.suite.groups;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.utils.Future;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.groups.commands.GroupCommand;
import dev.httpmarco.polocloud.suite.groups.storage.ClusterGroupStorage;
import dev.httpmarco.polocloud.suite.groups.storage.GlobalClusterGroupStorage;
import dev.httpmarco.polocloud.suite.groups.storage.LocalClusterGroupStorage;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Log4j2
public final class ClusterGroupProviderImpl implements ClusterGroupProvider {

    private final ClusterGroupStorage storage;

    public ClusterGroupProviderImpl() {
        this.storage = PolocloudSuite.instance().externalAccess() ? new GlobalClusterGroupStorage() : new LocalClusterGroupStorage();
        this.storage.initialize();

        // register global group command
        PolocloudSuite.instance().commandService().registerCommand(new GroupCommand(this));

        log.info("Load {} groups&8: &f{}", this.storage.groups().size(), String.join(", ", this.storage.groups().stream().map(Named::name).toList()));
    }

    @Contract(" -> new")
    @Override
    public @NotNull Future<Collection<ClusterGroup>> findAllAsync() {
        return Future.completedFuture(storage.groups());
    }

    @Contract("_ -> new")
    @Override
    public @NotNull Future<ClusterGroup> findAsync(String groupId) {
        return Future.completedFuture(this.storage.singleton(groupId));
    }

    @Override
    public void delete(String group) {
        this.storage.destroy(group);
    }

    @Override
    public ClusterGroupBuilder create(String group) {
        //todo
        return null;
    }
}
