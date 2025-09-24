package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Statistics
import dev.httpmarco.polocloud.agent.runtime.abstract.AbstractServiceStatsThread
import dev.httpmarco.polocloud.common.math.convertBytesToMegabytes
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.atomic.AtomicReference

class DockerServiceStatsThread(val client: DockerClient) : AbstractServiceStatsThread<DockerService>() {

    override fun detectService(service: DockerService) {
        val containerId = service.containerId

        if (containerId == null) {
            return
        }

        val statsRef = AtomicReference<Statistics>()
        val stats = getStats(containerId)

        // --- CPU ---
        val cpuDelta = stats!!.cpuStats.cpuUsage!!.totalUsage!! - stats.preCpuStats.cpuUsage!!.totalUsage!!
        val systemDelta = stats.cpuStats.systemCpuUsage!! - stats.preCpuStats.systemCpuUsage!!
        val cpuPercent = if (systemDelta > 0 && cpuDelta > 0) {
            (cpuDelta.toDouble() / systemDelta.toDouble()) * stats.cpuStats.onlineCpus!!.toDouble() * 100.0
        } else {
            0.0
        }

        service.updateCpuUsage(BigDecimal(cpuPercent).setScale(2, RoundingMode.HALF_UP).toDouble())

        // --- Memory ---
        val usedMemory = stats.memoryStats.usage
        service.updateMemoryUsage(convertBytesToMegabytes(usedMemory))
    }

    private fun getStats(containerId: String): Statistics? {
        var result: Statistics? = null

        val callback = object : ResultCallback.Adapter<Statistics>() {
            override fun onNext(stats: Statistics) {
                result = stats
                this.close() // Stream direkt schließen
            }
        }

        client.statsCmd(containerId)
            .withNoStream(true)
            .exec(callback)
            .awaitCompletion() // blockt bis close()

        return result
    }
}