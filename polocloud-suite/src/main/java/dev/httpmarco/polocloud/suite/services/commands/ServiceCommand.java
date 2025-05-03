package dev.httpmarco.polocloud.suite.services.commands;

import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.ClusterServiceArgument;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ServiceCommand extends Command {

    public ServiceCommand(ClusterServiceProvider serviceProvider) {
        super("service", "Manage all your services", "services");


        // service list
        // service <name>
        // service <name> stop
        // service <name> log
        // service <name> execute <command>

        syntax(commandContext -> {

            if(serviceProvider.findAll().isEmpty()) {
                log.info("Current there are no services running&8!");
                return;
            }

            log.info("Current running services:");
            for (var service : serviceProvider.findAll()) {
                log.info("&8- &f{}", service.name());
            }
        }, new KeywordArgument("list"));

        var serviceArgument = new ClusterServiceArgument("service");


        syntax(commandContext -> {
            var service = commandContext.arg(serviceArgument);

            if(service == null) {
                log.info("Service not found&8!");
                return;
            }

            log.info(service.name());
            log.info("&8  ├─ &7Id: &f{}", service.uniqueId());
            log.info("&8  ├─ &7Group: &f{}", service.group().name());
            log.info("&8  ├─ &7State: &f{}", service.state().name());
        }, serviceArgument);

    }
}
