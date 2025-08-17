package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.common.math.convertBytesToMegabytes
import oshi.SystemInfo
import java.math.BigDecimal
import java.math.RoundingMode

class LocalCloudInformationThread : Thread("polocloud-local-cloud-information") {

    override fun run() {
        while (true) {
            Agent.cloudInformationStorage.saveCurrentCloudInformation()

            try {
                sleep(5000)
            } catch (_: InterruptedException) {
                interrupt()
                break
            }
        }
    }
}