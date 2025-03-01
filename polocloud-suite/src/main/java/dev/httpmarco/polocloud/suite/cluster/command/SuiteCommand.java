package dev.httpmarco.polocloud.suite.cluster.command;

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

        var nameArgument = new TextArgument("name");
        var hostnameArgument = new TextArgument("hostname");
        var portArgument = new IntArgument("port");
        var clusterToken = new TextArgument("token");

        syntax(commandContext -> {
            var externalSuiteData = new SuiteData(commandContext.arg(nameArgument), commandContext.arg(hostnameArgument), commandContext.arg(portArgument));
            var externalSuite = new ExternalSuite(externalSuiteData);


            // health check -> is online
            if(!externalSuite.available()) {
                log.warn("Suite " + externalSuiteData.id() + " is not available! The suite must be reachable to register it!");
                return;
            }

            log.info("Registering suite " + externalSuiteData.id() + " with hostname " + externalSuiteData.hostname() + " and port " + externalSuiteData.port() + ":");
        }, new KeywordArgument("bind"), nameArgument, hostnameArgument, portArgument, clusterToken);


    }
}
