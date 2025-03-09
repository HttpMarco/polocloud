package dev.httpmarco.polocloud.suite.cluster;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import dev.httpmarco.polocloud.suite.cluster.configuration.redis.RedisConfig;
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
        if (config instanceof ClusterGlobalConfig) {
            log.info("Use &fglobal &7cluster profile.");
            //return new GlobalCluster();
            return null;//todo
        } else {
            log.info("Use &flocal &7cluster profile.");
            return new LocalCluster();
        }
    }

    public void createNewCluster(String hostname, int port, String username, String password, int database) {
        var token = UUID.randomUUID().toString().substring(0, 16);

        switchClusterState(token, hostname, port, username, password, database);
    }

    private void switchClusterState(String token, String hostname, int port, String username, String password, int database) {
        var redisConfig = new RedisConfig(hostname, port, username, password, database);
        var redisClient = new RedisClient(redisConfig);

        if (!redisClient.available()) {
            log.warn("The cluster can only be created if the redis server is available!");
            return;
        }

        var privateKey = UUID.randomUUID().toString().substring(0, 10);
        var config = PolocloudSuite.instance().config();
        var currentConfig = config.cluster();

        // update the config for global cluster
        var globalClusterConfig = new ClusterGlobalConfig(currentConfig.id(), "0.0.0.0", currentConfig.port(), token, privateKey, redisConfig);
        config.cluster(globalClusterConfig);
        config.update();

        var globalCluster = new GlobalCluster(globalClusterConfig, redisClient);

        // sync own cluster point data to redis
        globalCluster.syncStorage().push(new ClusterSuiteData(globalClusterConfig.id(), globalClusterConfig.hostname(), privateKey, globalClusterConfig.port()));

        PolocloudSuite.instance().updateCluster(globalCluster);
        log.info("Successfully created global cluster instance!");
    }
}
