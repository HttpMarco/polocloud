package dev.httpmarco.polocloud.suite.cluster;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterLocalConfig;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.global.GlobalCluster;
import dev.httpmarco.polocloud.suite.cluster.global.suites.ExternalSuite;
import dev.httpmarco.polocloud.suite.cluster.local.LocalCluster;
import dev.httpmarco.polocloud.suite.utils.redis.RedisClient;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@UtilityClass
public class ClusterInitializer {

    private static final Logger log = LogManager.getLogger(ClusterInitializer.class);

    public Cluster generate(ClusterConfig config) {
        if (config instanceof ClusterGlobalConfig clusterGlobalConfig) {
            log.info("Use &fglobal &7cluster profile.");

            var redisClient = new RedisClient(clusterGlobalConfig.redis());

            if (!redisClient.available()) {
                log.error("Redis is not available! Please check your redis configuration!");
                System.exit(-1);
                return null;
            }
            var cluster = new GlobalCluster(clusterGlobalConfig, redisClient);
            cluster.initializeExternals();
            return cluster;
        } else {
            log.info("Use &flocal &7cluster profile.");
            return new LocalCluster();
        }
    }

    public GlobalCluster switchToGlobalCluster(@NotNull RedisClient redisClient, String token) {
        var privateKey = UUID.randomUUID().toString().substring(0, 10);
        var polocloudSuite = PolocloudSuite.instance();
        var config = polocloudSuite.config();
        var currentConfig = config.cluster();

        // update the config for global cluster
        var globalClusterConfig = new ClusterGlobalConfig(currentConfig.id(), "0.0.0.0", currentConfig.port(), token, privateKey, redisClient.redisConfig());
        config.cluster(globalClusterConfig);
        config.update();

        var globalCluster = new GlobalCluster(globalClusterConfig, redisClient);

        // sync own cluster point data to redis
        globalCluster.syncStorage().push(new ClusterSuiteData(globalClusterConfig.id(), globalClusterConfig.hostname(), privateKey, globalClusterConfig.port()));

        polocloudSuite.updateCluster(globalCluster);
        // update the command manager
        polocloudSuite.commandService().refreshCommandContext();
        return globalCluster;
    }

    public LocalCluster switchToLocalCluster() {
        var polocloudSuite = PolocloudSuite.instance();
        if (polocloudSuite.cluster() instanceof GlobalCluster globalCluster) {
            var config = polocloudSuite.config();
            var currentConfig = config.cluster();
            var localCluster = new LocalCluster();
            var localClusterConfig = new ClusterLocalConfig(currentConfig.id(), currentConfig.port());

            globalCluster.syncStorage().delete(globalCluster.localSuite().data());

            // call other clusters to update their cluster point data
            for (ExternalSuite suite : globalCluster.suites()) {

                if (!suite.available()) {
                    // we call only to online suites
                    continue;
                }

                // say goodbye to the other suites
                suite.clusterStub().drainCluster(ClusterService.SuiteDrainRequest.newBuilder().setId(config.cluster().id()).build());
            }

            config.cluster(localClusterConfig);
            config.update();

            polocloudSuite.updateCluster(localCluster);
            // update the command manager
            polocloudSuite.commandService().refreshCommandContext();
            return localCluster;
        }
        log.warn("The cluster is already a local cluster!");
        return null;
    }
}
