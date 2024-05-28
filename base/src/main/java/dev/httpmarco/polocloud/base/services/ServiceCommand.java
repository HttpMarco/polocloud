package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.logging.Logger;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;

@Command(command = "service", aliases = {"services", "ser"}, description = "Manage all your online services")
public final class ServiceCommand {
    private final Logger logger = CloudAPI.instance().logger();

    @DefaultCommand
    public void handle() {
        logger.info("&3service &2<&1name&2> &2- &1All specific information&2.");
        logger.info("&3service &2<&1name&2> &1log &2- &1Get the last not read log lines&2.");
        logger.info("&3service &2<&1name&2> &1screen &2- &1Join into a service console&2.");
        logger.info("&3service &2<&1name&2> &1shutdown &2- &1Shutdown a specific service&2.");
        logger.info("&3service &2<&1name&2> &1execute &2<&1command...&2> &2- &1Execute a specific command on a service&2.");
        logger.info("&3service &2<&1name&2> &1property set &2<&1key&2> &2<&1value&2> &2- &1Attach a new service property&2.");
        logger.info("&3service &2<&1name&2> &1property remove &2<&1key&2> &2- &1Remove a existing property&2.");
    }

    @SubCommand(args = {"list"})
    public void handleList() {
        var services = CloudAPI.instance().serviceProvider().services();
        logger.info("Following &3" + services.size() + " &1services are online&2:");
        services.forEach(service -> logger.info("&2- &4" + service.name() + "&2: (&1" + service + "&2)"));
    }

    @SubCommand(args = {"<name>"})
    public void handleInfo(String name) {

        var service = CloudAPI.instance().serviceProvider().service(name);

        if (service == null) {
            logger.info("This services does not exists&2!");
            return;
        }

        logger.info("Name&2: &3" + name);
        logger.info("Platform&2: &3" + service.group().platform().version());
        logger.info("Current memory&2: &3-1");
        logger.info("Players&2: &3-1");
        logger.info("Maximal players&2: &3" + service.maxPlayers());
        logger.info("Port &2: &3" + service.port());
        logger.info("State&2: &3" + service.state());
        logger.info("Properties &2(&1" + service.properties().properties().size() + "&2): &3");

        service.properties().properties().forEach((groupProperties, o) -> {
            logger.info("   &2- &1" + groupProperties.id() + " &2= &1" + o.toString());
        });

    }

    @SubCommand(args = {"<name>", "log"})
    public void handleLog(String name) {
        var service = CloudAPI.instance().serviceProvider().service(name);

        if (service == null) {
            logger.info("This services does not exists&2!");
            return;
        }

        for (var log : service.log()) {
            logger.info("&3" + name + "&2: &1" + log);
        }
    }

    @SubCommand(args = {"<name>", "screen"})
    public void handleScreen(String name) {
        var service = CloudAPI.instance().serviceProvider().service(name);
        if (service == null) {
            logger.info("This services does not exists&2!");
            return;
        }
        ((LocalCloudService) service).subscribeLog();
    }

    @SubCommand(args = {"<name>", "shutdown"})
    public void handleShutdown(String name) {
        var service = CloudAPI.instance().serviceProvider().service(name);
        if (service == null) {
            logger.info("This services does not exists&2!");
            return;
        }
        service.shutdown();
    }
}
