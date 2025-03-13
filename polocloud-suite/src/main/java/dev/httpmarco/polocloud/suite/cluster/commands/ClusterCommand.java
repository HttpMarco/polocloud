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
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ClusterCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClusterCommand.class);

    public ClusterCommand() {
        super("cluster", "The main command for cluster management");

        var cluster = PolocloudSuite.instance().cluster();

        syntax(commandContext -> {

            log.info("Using cluster profile: &f{}", cluster instanceof LocalCluster ? "Local" : "Global");
            log.info(" ");

            if (cluster instanceof GlobalCluster globalCluster) {
                log.info("Local suite&8:");
                log.info("  &8- &7Id&8: &f{}", globalCluster.localSuite().id());
                log.info("  &8- &7Status&8: &f{}", globalCluster.state());
                log.info("  &8- &7Address&8: &f{}", globalCluster.localSuite().data().address());
                log.info("  &8- &7Private key: &f{}", "*".repeat(8) + globalCluster.localSuite().data().privateKey().substring(8));
                log.info(" ");
                log.info("External suites &8(&7{}&8)&8:", globalCluster.suites().size());

                for (int i = 0; i < globalCluster.suites().size(); i++) {
                    var suite = globalCluster.suites().get(i);
                    String prefix = "├";

                    // the last element of suites
                    if (i == globalCluster.suites().size() - 1) {
                        prefix = "└";
                    }

                    log.info("  &8{} &f{}", prefix, suiteStateColorCode(suite) + suite.data().id() + " &8(&7" + suite.data().address() + "&8)");
                }
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

                        if (checkRedisAvailable(redisClient)) {
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

                externalSuite.close();
                var redisClient = new RedisClient(new RedisConfig(it.arg(redisHostname), it.arg(redisPort), it.arg(redisUsername), it.arg(redisPassword), it.arg(redisDatabase)));

                // we only can work if the redis client is connected to the same redis server
                if (checkRedisAvailable(redisClient)) {
                    return;
                }

                if (!redisClient.has("polocloud-cluster-" + result.getToken())) {
                    log.warn("The redis database is not the same as the cluster you are trying to join! Use the same!");
                    return;
                }

                var globalCluster = ClusterInitializer.switchToGlobalCluster(redisClient, result.getToken());
                // scan all existing suites
                globalCluster.initializeExternals();

                // append this new suite to the cluster suites cache
                for (var suite : globalCluster.suites()) {
                    if (suite.available()) {
                        suite.clusterStub().runtimeHandshake(ClusterService.SuiteRuntimeHandShakeRequest.newBuilder()
                                .setId(globalCluster.localSuite().id())
                                .setHostname(globalCluster.localSuite().data().hostname())
                                .setPort(globalCluster.localSuite().data().port())
                                .setPrivateKey(globalCluster.localSuite().data().privateKey())
                                .build());
                    }
                }


                log.info("Successfully joined the cluster!");
            }, "Join an existing cluster", new KeywordArgument("enter"), id, hostname, port, privateKey, redisHostname, redisPort, redisUsername, redisPassword, redisDatabase);
        }

        if (cluster instanceof GlobalCluster globalCluster) {
            syntax(commandContext -> {

                if (!globalCluster.syncStorage().available()) {
                    log.warn("You can only disconnect from the cluster if the redis server is available!");
                    return;
                }

                LocalCluster localCluster = ClusterInitializer.switchToLocalCluster();
                log.info("Successfully disconnected from the cluster! Now we run in local mode!");
            }, "Disconnect from the current cluster and change to a local cluster", new KeywordArgument("drain"));
        }
    }

    private boolean checkRedisAvailable(@NotNull RedisClient client) {
        if (!client.available()) {
            log.warn("The cluster can only be created if the redis server is available!");
            return false;
        }
        return true;
    }

    private String suiteStateColorCode(ExternalSuite suite) {
        return switch (suite.state()) {
            case AVAILABLE -> "&b";
            case OFFLINE -> "&8";
            case INITIALIZING -> "&e";
            case INVALID -> "&c";
            case UNRECOGNIZED -> "&7";
        };
    }
}
