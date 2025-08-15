package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.common.math.convertBytesToMegabytes
import oshi.SystemInfo
import java.math.BigDecimal
import java.math.RoundingMode

class LocalCpuDetectionThread : Thread("polocloud-local-cpu-detection") {

    private val os = SystemInfo().operatingSystem

    override fun run() {
        while (true) {
            Agent.runtime.serviceStorage().findAll().forEach {
                detectService(it as LocalService)
            }

            try {
                sleep(1000)
            } catch (_: InterruptedException) {
                interrupt()
                break
            }
        }
    }

    fun detectService(service: LocalService) {

        if (service.pid() == null) {
            return
        }

        if (service.lastCpuSnapshot == null) {
            service.lastCpuSnapshot = os.getProcess(service.pid()!!.toInt());
            return
        }

        val currentSnapshot = os!!.getProcess(service.pid()!!.toInt())

        if (currentSnapshot != null) {
            service.lastCpuSnapshot = currentSnapshot
            val cpu = currentSnapshot.getProcessCpuLoadBetweenTicks(service.lastCpuSnapshot)

            service.lastCpuUpdateTimeStamp = System.nanoTime()

            service.updateCpuUsage(BigDecimal(cpu).setScale(2, RoundingMode.HALF_UP).toDouble())
            service.updateMemoryUsage(convertBytesToMegabytes(currentSnapshot.residentSetSize))
        }
    }
}