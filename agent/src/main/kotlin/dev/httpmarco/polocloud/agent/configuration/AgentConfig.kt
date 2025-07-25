package dev.httpmarco.polocloud.agent.configuration

import java.util.Locale

data class AgentConfig(
    var locale: Locale = Locale.ENGLISH,
    var autoUpdate: Boolean = true,
    var port: Int = 8932
) : Config