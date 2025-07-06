package dev.httpmarco.polocloud.agent.runtime.local.detector

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.detector.Detector
import dev.httpmarco.polocloud.agent.runtime.local.LocalService
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class LocalLogDetector : Detector {

    override fun tick() {
        Agent.instance.runtime.serviceStorage().items().forEach { service ->
            if (service is LocalService) {
                service.process?.inputStream?.let { inputStream ->
                    BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
                        reader.lines().forEach { line -> service.cachedLogs.add(line) }
                    }
                }
            }
        }
    }

    override fun cycleLife(): Long {
        return 100
    }
}
