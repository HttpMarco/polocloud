package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;

public final class ServiceCommand {

    @Command(command = "service", aliases = {"services", "ser"})
    public void handle() {
        CloudAPI.instance().logger().info("&3service &2<&1name&2> &2- &1All specific information&2.");
        CloudAPI.instance().logger().info("&3service &2<&1name&2> &1log &2- &1Get the last not read log lines&2.");
        CloudAPI.instance().logger().info("&3service &2<&1name&2> &1screen &2- &1Join into a service console&2.");
        CloudAPI.instance().logger().info("&3service &2<&1name&2> &1shutdown &2- &1Shutdown a specific service&2.");
        CloudAPI.instance().logger().info("&3service &2<&1name&2> &1execute &2<&1command...&2> &2- &1Execute a specific command on a service&2.");
        CloudAPI.instance().logger().info("&3service &2<&1name&2> &1property set &2<&1key&2> &2<&1value&2> &2- &1Attach a new service property&2.");
        CloudAPI.instance().logger().info("&3service &2<&1name&2> &1property remove &2<&1key&2> &2- &1Remove a existing property&2.");
    }

    @SubCommand(args = {"<name>", "log"})
    public void handleSub(String name) {
        var service = CloudAPI.instance().serviceProvider().service(name);

        if (service == null) {
            CloudAPI.instance().logger().info("This services does not exists&2!");
            return;
        }

        for (var log : service.log()) {
            CloudAPI.instance().logger().info("&3" + name + " &2: &1" + log);
        }
    }
}
