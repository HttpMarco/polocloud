package dev.httpmarco.polocloud.suite.cluster.global;

import dev.httpmarco.polocloud.suite.cluster.Cluster;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import dev.httpmarco.polocloud.suite.cluster.global.suites.ExternalSuite;
import dev.httpmarco.polocloud.suite.cluster.global.suites.LocalSuite;
import dev.httpmarco.polocloud.suite.cluster.global.syncstorage.ClusterDataSyncStorage;
import dev.httpmarco.polocloud.suite.cluster.global.syncstorage.SyncStorage;
import dev.httpmarco.polocloud.suite.utils.redis.RedisClient;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class GlobalCluster implements Cluster {

    private final SyncStorage<ClusterSuiteData> syncStorage;

    private final LocalSuite localCluster;
    private final List<ExternalSuite> suites = new ArrayList<>();

    private ClusterSuite headSuite;

    public GlobalCluster(ClusterGlobalConfig config, RedisClient redisClient) {
        this.syncStorage = new ClusterDataSyncStorage(config.token(), redisClient);

        this.localCluster = new LocalSuite(new ClusterSuiteData(config.id(), config.hostname(), config.privateKey(), config.port()));
    }
}
