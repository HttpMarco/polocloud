package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.impl

import dev.httpmarco.polocloud.agent.configuration.AgentConfig
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.LocaleArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.TextArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.Setup
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.SetupStep

class OnboardingSetup : Setup<AgentConfig>("Onboarding Setup") {

    private val localeArgument = LocaleArgument("locale")
    private val updateArgument = TextArgument("auto-update") // TODO select menu

    override fun bindQuestion() {
        attach(SetupStep("agent.local-runtime.setup.onboarding.locale", localeArgument))
        attach(SetupStep("agent.local-runtime.setup.onboarding.auto-update", updateArgument))
    }

    override fun onComplete(result: InputContext): AgentConfig {
        val locale = result.arg(localeArgument)
        val autoUpdate = result.arg(updateArgument)

        val config = AgentConfig(
            locale = locale,
            autoUpdate = autoUpdate.toBoolean()
        )

        // TODO update config

        return config
    }

}