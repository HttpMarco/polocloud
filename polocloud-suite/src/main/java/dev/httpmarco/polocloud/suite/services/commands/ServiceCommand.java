package dev.httpmarco.polocloud.suite.services.commands;

import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.suite.commands.Command;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ServiceCommand extends Command {

    public ServiceCommand(ClusterServiceProvider serviceProvider) {
        super("service", "Manage all your services", "services");

        syntax(commandContext -> {

            if(serviceProvider.findAll().isEmpty()) {
                log.info("Current there are no services running&8!");
                return;
            }

            log.info("Current running services:");
            for (var service : serviceProvider.findAll()) {
                log.info("&8- &f{}", service.name());
            }
        });
    }
}
