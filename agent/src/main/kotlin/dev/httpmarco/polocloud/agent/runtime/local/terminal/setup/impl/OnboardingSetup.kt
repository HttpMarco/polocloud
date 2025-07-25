package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.impl

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.configuration.AgentConfig
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.IntArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.LocaleArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.TextArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.Setup
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.SetupStep

class OnboardingSetup : Setup<AgentConfig>("Onboarding Setup") {

    private val localeArgument = LocaleArgument("locale")
    private val updateArgument = TextArgument("auto-update") // TODO select menu
    private val portArgument = IntArgument("port")

    override fun bindQuestion() {
        attach(SetupStep("agent.local-runtime.setup.onboarding.locale", localeArgument) { locale ->
                i18n.overrideLocale(locale) // todo implement action
            })
        attach(SetupStep("agent.local-runtime.setup.onboarding.auto-update", updateArgument)) // TODO change language after selection
        attach(SetupStep("agent.local-runtime.setup.onboarding.port", portArgument))
    }

    override fun onComplete(result: InputContext): AgentConfig {
        val locale = result.arg(localeArgument)
        val autoUpdate = result.arg(updateArgument)
        val port = result.arg(portArgument) // TODO actually implement the port
        // TODO start the setup before launching the agent

        val config = Agent.instance.config
        config.locale = locale
        config.autoUpdate = autoUpdate.toBoolean()
        config.port = port

        config.save("config")

        return config
    }

}