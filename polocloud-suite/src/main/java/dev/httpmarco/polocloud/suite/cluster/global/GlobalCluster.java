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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class GlobalCluster implements Cluster {

    private static final Logger log = LogManager.getLogger(GlobalCluster.class);
    private final SyncStorage<ClusterSuiteData> syncStorage;

    private final LocalSuite localSuite;
    private final List<ExternalSuite> suites = new ArrayList<>();

    public GlobalCluster(ClusterGlobalConfig config, RedisClient redisClient) {
        this.syncStorage = new ClusterDataSyncStorage(config.token(), redisClient);
        this.localSuite = new LocalSuite(new ClusterSuiteData(config.id(), config.hostname(), config.privateKey(), config.port()));

        // scan all existing suites
        var entries = this.syncStorage.entries();

        if (entries.stream().noneMatch(clusterSuiteData -> clusterSuiteData.privateKey().equals(config.privateKey()))) {
            log.warn("The cluster is not correctly configured! The private key is not in the redis storage! -> No part of cluster!");
            // close the local suite
            System.exit(-1);
            return;
        }

        for (var entry : entries) {

            if (entry.privateKey().equals(config.privateKey())) {
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
    }
}
