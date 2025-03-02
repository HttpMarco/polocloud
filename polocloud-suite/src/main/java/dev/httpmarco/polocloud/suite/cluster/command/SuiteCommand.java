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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SuiteCommand extends Command {

    private static final Logger log = LogManager.getLogger(SuiteCommand.class);

    public SuiteCommand() {
        super("suite", "Manage all suites");

        var clusterProvider = PolocloudSuite.instance().clusterProvider();

        var nameArgument = new TextArgument("name");
        var hostnameArgument = new TextArgument("hostname");
        var portArgument = new IntArgument("port");
        var clusterToken = new TextArgument("token");

        syntax(commandContext -> {
            var externalSuiteData = new SuiteData(commandContext.arg(nameArgument), commandContext.arg(hostnameArgument), commandContext.arg(portArgument));
            var externalSuite = new ExternalSuite(externalSuiteData);

            if (clusterProvider.local().data().id().equalsIgnoreCase(externalSuiteData.id()) || clusterProvider.suites().stream().anyMatch(suite -> suite.data().id().equalsIgnoreCase(externalSuiteData.id()))) {
                log.warn("The suite {} is already a part of the cluster&8! &7Every suite need a unique id&8!", externalSuiteData.id());
                log.warn("Change the duplicated id in the external suite config&8.&7json&8!");
                return;
            }

            //todo check network adress

            // health check -> is online
            if (!externalSuite.available()) {
                log.warn("Suite {} is not available! The suite must be reachable to register it!", externalSuiteData.id());
                return;
            }

            var token = commandContext.arg(clusterToken);
            ClusterService.AuthClusterResponse response = externalSuite.clusterStub().auth(ClusterService.AuthIdentificationRequest.newBuilder()
                    .setClusterToken(token)
                    .setSuiteHostname(externalSuiteData.hostname())
                    .setSuitePort(externalSuiteData.port())
                    .setSuiteId(externalSuiteData.id())
                    .build());

            if (!response.getSuccess()) {
                log.warn("Failed to authenticate suite {}! Reason: {}", externalSuiteData.id(), response.getMessage());
                return;
            }

            log.info("Registering suite {} with hostname {} and port {}:", externalSuiteData.id(), externalSuiteData.hostname(), externalSuiteData.port());
        }, new KeywordArgument("bind"), nameArgument, hostnameArgument, portArgument, clusterToken);


        syntax(commandContext -> {
            //todo list suite
        }, new KeywordArgument("list"));

        syntax(commandContext -> {
            // todo remove suite
        }, new KeywordArgument("remove"), nameArgument);
    }
}
