package dev.httpmarco.polocloud.suite.cluster.commands;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.ClusterInitializer;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.global.suites.ExternalSuite;
import dev.httpmarco.polocloud.suite.cluster.local.LocalCluster;
import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.IntArgument;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import dev.httpmarco.polocloud.suite.commands.type.TextArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClusterCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClusterCommand.class);

    public ClusterCommand() {
        super("cluster", "The main command for cluster management");

        var cluster = PolocloudSuite.instance().cluster();

        syntax(commandContext -> {

        }, new KeywordArgument("info"));


        if (cluster instanceof LocalCluster) {

            var redisHostname = new TextArgument("redis-hostname");
            var redisPort = new IntArgument("redis-port");
            var redisUsername = new TextArgument("redis-username");
            var redisPassword = new TextArgument("redis-password");
            var redisDatabase = new IntArgument("redis-database");

            // create new cluster
            syntax(it -> ClusterInitializer.createNewCluster(it.arg(redisHostname), it.arg(redisPort), it.arg(redisUsername), it.arg(redisPassword), it.arg(redisDatabase))
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

                var result = externalSuite.clusterStub().attachSuite(ClusterService.ClusterSuiteAttachRequest.newBuilder().setSuiteId(data.id()).build());

                if (!result.getSuccess()) {
                    log.warn("Failed to enter the suite cluster: {}", result.getMessage());
                    return;
                }

                // todo add the suite to the cluster

            }, "Join an existing cluster", new KeywordArgument("enter"), id, hostname, port, privateKey);
        }
    }
}
