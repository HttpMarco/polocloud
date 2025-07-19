package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeServiceStorage
import dev.httpmarco.polocloud.agent.runtime.local.terminal.Jline3Terminal
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.KeywordArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.ServiceArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.type.StringArrayArgument
import kotlin.collections.component1
import kotlin.collections.component2

class ServiceCommand(private val serviceStorage: RuntimeServiceStorage, terminal: Jline3Terminal) :
    Command("service", "Used to manage services", "ser") {

    init {
        syntax(execution = { context ->
            if (serviceStorage.items().isEmpty()) {
                logger.info("No service found.")
                return@syntax
            }
            logger.info("Found ${serviceStorage.items().size} services&8:")
            serviceStorage.items().forEach { logger.info(" &8- &3${it.name()} &8(&7&8)") }
        }, KeywordArgument("list"))

        var serviceArgument = ServiceArgument()

        syntax(execution = {
            var service = it.arg(serviceArgument)

            logger.info("Service &3${service.name()}&8:")
            logger.info(" &8- &7State&8: &f${service.state}")
            logger.info(" &8- &7Group&8: &f${service.group.data.name}")
            logger.info(" &8- &7Port&8: &f${service.port}")
            logger.info(" &8- &7Hostname&8: &f${service.hostname}")
            logger.info(" &8- &7Players&8: &f${service.playerCount}&8/&f${service.maxPlayerCount}")
            logger.info(" &8- &7Properties&8:")

            service.properties.forEach { (key, value) ->
                logger.info("   &8- &7$key&8: &f$value")
            }
        }, serviceArgument)

        syntax(execution = {
            var service = it.arg(serviceArgument)

            service.shutdown()
        }, serviceArgument, KeywordArgument("shutdown"))

        syntax(execution = {
            var service = it.arg(serviceArgument)

            service.logs().forEach { log ->
                println(log)
            }
        }, serviceArgument, KeywordArgument("logs"))

        var commandArg = StringArrayArgument("command")
        syntax(execution = {
            var service = it.arg(serviceArgument)

            Agent.instance.runtime.expender().executeCommand(service, it.arg(commandArg))
            logger.info("Executed command on service &3${service.name()}&8: &f${it.arg(commandArg)}")
        }, serviceArgument, KeywordArgument("execute"), commandArg)

        syntax(execution = {
            val service = it.arg(serviceArgument)
            terminal.screenService.screenRecordingOf(service)
        }, serviceArgument, KeywordArgument("screen"))
    }
}