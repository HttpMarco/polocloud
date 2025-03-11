package dev.httpmarco.polocloud.suite.cluster.commands;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.ClusterInitializer;
import dev.httpmarco.polocloud.suite.cluster.configuration.redis.RedisConfig;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.global.GlobalCluster;
import dev.httpmarco.polocloud.suite.cluster.global.suites.ExternalSuite;
import dev.httpmarco.polocloud.suite.cluster.local.LocalCluster;
import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.IntArgument;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import dev.httpmarco.polocloud.suite.commands.type.TextArgument;
import dev.httpmarco.polocloud.suite.utils.redis.RedisClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class ClusterCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClusterCommand.class);

    public ClusterCommand() {
        super("cluster", "The main command for cluster management");

        var cluster = PolocloudSuite.instance().cluster();

        syntax(commandContext -> {
            if (cluster instanceof GlobalCluster globalCluster) {
                log.info("  &8- &7Local suite&8: &f{}", globalCluster.localSuite());
            }
        }, new KeywordArgument("info"));


        if (cluster instanceof LocalCluster) {

            var redisHostname = new TextArgument("redis-hostname");
            var redisPort = new IntArgument("redis-port");
            var redisUsername = new TextArgument("redis-username");
            var redisPassword = new TextArgument("redis-password");
            var redisDatabase = new IntArgument("redis-database");

            // create new cluster
            syntax(it -> {
                        var token = UUID.randomUUID().toString().substring(0, 16);
                        var redisClient = new RedisClient(new RedisConfig(it.arg(redisHostname), it.arg(redisPort), it.arg(redisUsername), it.arg(redisPassword), it.arg(redisDatabase)));

                        if (!redisClient.available()) {
                            log.warn("The cluster can only be created if the redis server is available!");
                            return;
                        }

                        ClusterInitializer.switchToGlobalCluster(redisClient, token);
                        log.info("Successfully created global cluster instance!");
                    }
                    , "Publish your first cluster instance"
                    , new KeywordArgument("open")
                    , redisHostname
                    , redisPort
                    , redisUsername
                    , redisPassword
                    , redisDatabase
            );

            var id = new TextArgument("suiteId");
            var hostname = new TextArgument("hostname");
            var port = new IntArgument("port");
            var privateKey = new TextArgument("privateKey");

            syntax(it -> {
                var data = new ClusterSuiteData(it.arg(id), it.arg(hostname), it.arg(privateKey), it.arg(port));
                var externalSuite = new ExternalSuite(data);

                if (!externalSuite.available()) {
                    log.warn("The cluster you are trying to join is not available! The part suite must be online!");
                    return;
                }

                var result = externalSuite.clusterStub().attachSuite(ClusterService.ClusterSuiteAttachRequest.newBuilder().setSuitePrivateKey(it.arg(privateKey)).setSuiteId(data.id()).build());

                if (!result.getSuccess()) {
                    log.warn("Failed to enter the suite cluster: {}", result.getMessage());
                    return;
                }

                var redisClient = new RedisClient(new RedisConfig(it.arg(redisHostname), it.arg(redisPort), it.arg(redisUsername), it.arg(redisPassword), it.arg(redisDatabase)));

                if (!redisClient.available()) {
                    log.warn("The cluster can only be created if the redis server is available!");
                    return;
                }

                if (!redisClient.has("polocloud-cluster-" + result.getToken())) {
                    log.warn("The redis database is not the same as the cluster you are trying to join! Use the same!");
                    return;
                }

                var globalCluster = ClusterInitializer.switchToGlobalCluster(redisClient, result.getToken());
                // scan all existing suites
                globalCluster.initializeExternals();

                // todo sync other nodes


                log.info("Successfully joined the cluster!");
            }, "Join an existing cluster", new KeywordArgument("enter"), id, hostname, port, privateKey, redisHostname, redisPort, redisUsername, redisPassword, redisDatabase);
        }
    }
}
