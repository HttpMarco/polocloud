package dev.httpmarco.polocloud.suite.cluster.global;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.Cluster;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import dev.httpmarco.polocloud.suite.cluster.global.suites.ExternalSuite;
import dev.httpmarco.polocloud.suite.cluster.global.suites.LocalSuite;
import dev.httpmarco.polocloud.suite.cluster.global.syncstorage.ClusterDataSyncStorage;
import dev.httpmarco.polocloud.suite.cluster.global.syncstorage.SyncStorage;
import dev.httpmarco.polocloud.suite.cluster.tasks.ClusterStatusTask;
import dev.httpmarco.polocloud.suite.utils.redis.RedisClient;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class GlobalCluster implements Cluster {

    private static final Logger log = LogManager.getLogger(GlobalCluster.class);
    private final SyncStorage<ClusterSuiteData> syncStorage;

    private ClusterService.State state = ClusterService.State.INITIALIZING;

    private final LocalSuite localSuite;
    private final List<ExternalSuite> suites = new ArrayList<>();

    private final ClusterStatusTask statusTask = new ClusterStatusTask(this);

    public GlobalCluster(ClusterGlobalConfig config, RedisClient redisClient) {
        this.syncStorage = new ClusterDataSyncStorage(config.token(), redisClient);
        this.localSuite = new LocalSuite(new ClusterSuiteData(config.id(), config.hostname(), config.privateKey(), config.port()));
    }

    public void initializeExternals() {
        // scan all existing suites
        var entries = this.syncStorage.entries();
        var config = PolocloudSuite.instance().config().cluster();

        if (config instanceof ClusterGlobalConfig globalConfig) {
            if (entries.stream().noneMatch(clusterSuiteData -> clusterSuiteData.privateKey().equals(globalConfig.privateKey()))) {
                log.warn(PolocloudSuite.instance().translation().get("cluster.globalCluster.notConfigured"));
                // close the local suite
                System.exit(-1);
                return;
            }

            for (var entry : entries) {
                if (entry.privateKey().equals(globalConfig.privateKey())) {
                    // check name and port -> on change update
                    if (!entry.id().equals(config.id()) || entry.port() != config.port()) {
                        this.syncStorage.update(entry);
                    }
                    // we only use the local suite
                    continue;
                }

                // register suite
                this.suites.add(new ExternalSuite(entry));
                // try connection to the suite and check state
            }
        } else {
            // this case should never happen
            log.error(PolocloudSuite.instance().translation().get("cluster.globalCluster.errorConfig"));
        }

        this.statusTask.start();

        log.info(PolocloudSuite.instance().translation().get("cluster.globalCluster.availableSuites"), String.join("&8, ", this.suites.stream().map(it -> (it.state() == ClusterService.State.AVAILABLE ? "&f" : "&8") + it.id()).toList()));

        // now we can start the show!
        this.state = ClusterService.State.AVAILABLE;
    }

    @Override
    public void close() {
        this.localSuite.close();
    }

    public ExternalSuite find(String id) {
        return this.suites.stream().filter(s -> s.id().equals(id)).findFirst().orElse(null);
    }

    @Override
    public String name() {
        return "global";
    }
}
