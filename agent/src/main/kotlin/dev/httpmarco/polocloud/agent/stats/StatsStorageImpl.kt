package dev.httpmarco.polocloud.agent.stats

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.common.os.cpuUsage
import dev.httpmarco.polocloud.common.os.usedMemory
import dev.httpmarco.polocloud.shared.stats.SharedStatsProvider
import dev.httpmarco.polocloud.shared.stats.Stats

private val started = Agent.runtime.started()
private val cpuUsage = cpuUsage()
private val usedMemory = usedMemory()

class StatsStorageImpl : SharedStatsProvider<Stats> {

    override fun get(): Stats {
        return Stats(started, cpuUsage, usedMemory)
    }

}