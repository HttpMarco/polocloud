package dev.httpmarco.polocloud.agent.configuration

import java.util.Locale

data class AgentConfig(
    val locale: Locale = Locale.ENGLISH,
    val autoUpdate: Boolean = true
) : Config // TODO port