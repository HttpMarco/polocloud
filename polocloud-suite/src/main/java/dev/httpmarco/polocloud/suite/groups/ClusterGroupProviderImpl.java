package dev.httpmarco.polocloud.suite.groups;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.utils.Future;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.storage.ClusterStorage;
import dev.httpmarco.polocloud.suite.groups.commands.GroupCommand;
import dev.httpmarco.polocloud.suite.groups.storage.LocalClusterGroupStorage;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Log4j2
public final class ClusterGroupProviderImpl implements ClusterGroupProvider {

    private final ClusterStorage<String, ClusterGroup> storage;

    public ClusterGroupProviderImpl() {
        //todo
      //  this.storage = PolocloudSuite.instance().externalAccess() ? new GlobalClusterGroupStorage() : new LocalClusterGroupStorage();
        this.storage = new LocalClusterGroupStorage();
        this.storage.initialize();

        // register global group command
        PolocloudSuite.instance().commandService().registerCommand(new GroupCommand(this));

        if (this.storage.items().isEmpty()) {
            return;
        }

        log.info(PolocloudSuite.instance().translation().get("suite.group.load", this.storage.items().size(), String.join(", ", this.storage.items().stream().map(Named::name).toList())));
    }

    @Contract(" -> new")
    @Override
    public @NotNull Future<Collection<ClusterGroup>> findAllAsync() {
        return Future.completedFuture(storage.items());
    }

    @Contract("_ -> new")
    @Override
    public @NotNull Future<ClusterGroup> findAsync(String groupId) {
        return Future.completedFuture(this.storage.singleton(groupId));
    }

    @Override
    public void delete(String group) {
        // stop all services
        for (var service : find(group).runningServices()) {
            service.shutdown();
        }

        this.storage.destroy(group);
    }

    @Override
    public ClusterGroupBuilder create(String group) {
        //todo
        return null;
    }

    public void registerGroup(ClusterGroup group) {
        this.storage.publish(group);
    }
}
