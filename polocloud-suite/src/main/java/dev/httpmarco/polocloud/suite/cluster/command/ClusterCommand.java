package dev.httpmarco.polocloud.suite.cluster.command;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import dev.httpmarco.polocloud.suite.cluster.suits.ExternalSuite;
import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.IntArgument;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import dev.httpmarco.polocloud.suite.commands.type.TextArgument;
import dev.httpmarco.polocloud.suite.configuration.ClusterConfig;
import dev.httpmarco.polocloud.suite.utils.DateFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public final class ClusterCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClusterCommand.class);

    public ClusterCommand(ClusterProvider clusterProvider) {
        super("cluster", "Manage the hole cluster");

        var nameArgument = new TextArgument("name");
        var hostnameArgument = new TextArgument("hostname");
        var portArgument = new IntArgument("port");
        var privateKey = new TextArgument("privateKey");

        syntax(commandContext -> {
            var externalSuiteData = new SuiteData(commandContext.arg(nameArgument), commandContext.arg(hostnameArgument), commandContext.arg(portArgument));
            var externalSuite = new ExternalSuite(externalSuiteData);

            /*
            if (clusterProvider.local().data().id().equalsIgnoreCase(externalSuiteData.id()) || clusterProvider.suites().stream().anyMatch(suite -> suite.data().id().equalsIgnoreCase(externalSuiteData.id()))) {
                log.warn("The suite {} is already a part of the cluster&8! &7Every suite need a unique id&8!", externalSuiteData.id());
                log.warn("Change the duplicated id in the external suite config&8.&7json&8!");
                return;
            }

             */

            //todo check network adress

            // health check -> is online
            if (!externalSuite.available()) {
                log.warn("Suite {} is not available! The suite must be reachable to register it!", externalSuiteData.id());
                return;
            }

            ClusterConfig clusterConfig = PolocloudSuite.instance().config().cluster();
            var clusterToken = clusterConfig.clusterToken();

            if (clusterToken == null) {
                clusterToken = UUID.randomUUID().toString();
            }

            var privateToken = commandContext.arg(privateKey);
            var response = externalSuite.clusterStub().attachSuite(ClusterService.ClusterSuiteAttachRequest.newBuilder()
                    .setClusterToken(clusterToken)
                    .setSuitePrivateKey(privateToken)
                    .setSuiteId(externalSuiteData.id())
                    .setBindSuiteId(clusterConfig.localSuite().id())
                    .setBindSuiteHostname(clusterConfig.localSuite().hostname())
                    .setBindSuitePort(clusterConfig.localSuite().port())
                    .build());

            if (!response.getSuccess()) {
                log.warn("Failed to authenticate suite {}! Reason: {}", externalSuiteData.id(), response.getMessage());
                return;
            }

            clusterProvider.suites().add(externalSuite);
            // update local configuration and append cluster token

            // todo save new cluster token if change

            log.info("Registering suite {}", externalSuiteData.id());
        }, new KeywordArgument("bind"), nameArgument, hostnameArgument, portArgument, privateKey);


        syntax(commandContext -> {
            log.info("Local suite information:");
            var localData = clusterProvider.local().data();
            log.info("&8   &7Id&8: &b{}", localData.id());
            log.info("&8   &7State&8: &f{}", clusterProvider.local().state().name());
            log.info("&8   &7Hostname&8: &f{}", localData.hostname());
            log.info("&8   &7Port&8: &f{}", localData.port());
            log.info("&8   &7Creation time&8: &f{}", DateFormatter.format(clusterProvider.local().infoSnapshot().creationTime()));
            log.info("&8   &7Last update time&8: &f{}", DateFormatter.format(clusterProvider.local().infoSnapshot().lastUpdateTime()));
            // empty line
            log.info(" ");

            log.info("Currently registered external suites &8(&f" + clusterProvider.suites().size() + "&8):");
            clusterProvider.suites().forEach(externalSuite -> log.info("&8 - &bSuite {} &8(&7{}&8:&7{}&8, &7connection state={}&8)", externalSuite.data().id(), externalSuite.data().hostname(), externalSuite.data().port(), externalSuite.available()));
        }, new KeywordArgument("info"));

        syntax(commandContext -> {
            // todo remove suite
        }, new KeywordArgument("remove"), nameArgument);
    }
}
