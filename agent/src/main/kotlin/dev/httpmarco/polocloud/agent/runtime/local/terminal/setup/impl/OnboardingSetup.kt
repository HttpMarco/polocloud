package dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.impl

import dev.httpmarco.polocloud.agent.configuration.AgentConfig
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.InputContext
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.LocaleArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.Setup
import dev.httpmarco.polocloud.agent.runtime.local.terminal.setup.SetupStep

class OnboardingSetup : Setup<AgentConfig>("Onboarding Setup") {

    private val localeArgument = LocaleArgument("locale")

    override fun bindQuestion() {
        attach(SetupStep("agent.local-runtime.setup.onboarding.locale", localeArgument))
    }

    override fun onComplete(result: InputContext): AgentConfig {
        val locale = result.arg(localeArgument)
        i18n.locale = locale


        return AgentConfig(
            locale = locale
        )
    }

}