package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;

public final class ServiceCommand {

    @Command(command = "service", aliases = {"services", "ser"})
    public void handle() {

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
