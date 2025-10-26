package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.KeywordArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command

class TemplateCommand : Command("templates", "Manage all your templates") {

    init {
        syntax(execution = {
            i18n.info("agent.terminal.command.templates.info.header", Agent.runtime.templateStorage().availableTemplates().size)
            Agent.runtime.templateStorage().availableTemplates().forEach { template ->
                i18n.info("agent.terminal.command.template.list", template.name, template.size())
            }
        }, KeywordArgument("list"))

    }
}