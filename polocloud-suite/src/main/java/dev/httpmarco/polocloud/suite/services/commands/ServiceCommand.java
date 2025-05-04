package dev.httpmarco.polocloud.suite.services.commands;

import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
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

        var translation = PolocloudSuite.instance().translation();

        syntax(commandContext -> {

            if(serviceProvider.findAll().isEmpty()) {
                log.info(translation.get("suite.command.service.noneRunning"));
                return;
            }

            log.info(translation.get("suite.command.service.list"));
            for (var service : serviceProvider.findAll()) {
                log.info("&8- &f{}", service.name());
            }
        }, new KeywordArgument("list"));

        var serviceArgument = new ClusterServiceArgument("service");


        syntax(commandContext -> {
            var service = commandContext.arg(serviceArgument);

            if(service == null) {
                log.info(translation.get("suite.command.service.notFound"));
                return;
            }

            log.info(service.name());
            log.info(translation.get("suite.command.service.info.id", service.uniqueId()));
            log.info(translation.get("suite.command.service.info.group", service.group().name()));
            log.info(translation.get("suite.command.service.info.state", service.state().name()));
        }, serviceArgument);

    }
}
