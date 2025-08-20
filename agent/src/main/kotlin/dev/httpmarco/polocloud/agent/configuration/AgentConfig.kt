package dev.httpmarco.polocloud.agent.configuration

import java.util.Locale

data class AgentConfig(
    var locale: Locale = Locale.ENGLISH,
    var autoUpdate: Boolean = true,
    var port: Int = 8932,
    //var statusLine: Boolean = true,
    var maxConcurrentServersStarts: Int = 4,
    var maxCachingProcesses: Int = 4,
    var maxCPUPercentageToStart: Double = 75.0
) : Config