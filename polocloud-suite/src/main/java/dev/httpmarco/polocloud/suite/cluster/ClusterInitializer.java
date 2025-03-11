package dev.httpmarco.polocloud.suite.cluster;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.global.GlobalCluster;
import dev.httpmarco.polocloud.suite.cluster.local.LocalCluster;
import dev.httpmarco.polocloud.suite.utils.redis.RedisClient;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public GlobalCluster switchToGlobalCluster(RedisClient redisClient, String token) {
        var privateKey = UUID.randomUUID().toString().substring(0, 10);
        var config = PolocloudSuite.instance().config();
        var currentConfig = config.cluster();

        // update the config for global cluster
        var globalClusterConfig = new ClusterGlobalConfig(currentConfig.id(), "0.0.0.0", currentConfig.port(), token, privateKey, redisClient.redisConfig());
        config.cluster(globalClusterConfig);
        config.update();

        var globalCluster = new GlobalCluster(globalClusterConfig, redisClient);

        // sync own cluster point data to redis
        globalCluster.syncStorage().push(new ClusterSuiteData(globalClusterConfig.id(), globalClusterConfig.hostname(), privateKey, globalClusterConfig.port()));

        PolocloudSuite.instance().updateCluster(globalCluster);
        return globalCluster;
    }
}
