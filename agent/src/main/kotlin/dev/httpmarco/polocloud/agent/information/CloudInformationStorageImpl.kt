package dev.httpmarco.polocloud.agent.information

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.common.os.cpuUsage
import dev.httpmarco.polocloud.common.os.usedMemory
import dev.httpmarco.polocloud.shared.information.CloudInformation

private val cachedInformation =  mutableListOf<CloudStatistic>()

class CloudInformationStorageImpl : CloudInformationStorage {

    override fun find(): CloudInformation {
        return cachedInformation.last().toCloudInformation()
    }

    override fun find(from: Long, to: Long): List<CloudInformation> {
        return cachedInformation
            .filter { it.timestamp in from..to }
            .map { it.toCloudInformation() }
    }

    override fun findAll(): List<CloudInformation> {
        return cachedInformation.map { it.toCloudInformation() }
    }

    override fun addCloudInformation(cloudInformation: CloudInformation) {
        cachedInformation.add(CloudStatistic.bindCloudInformation(cloudInformation))
    }

    override fun removeCloudInformation(cloudInformation: CloudInformation) {
        cachedInformation.remove(CloudStatistic.bindCloudInformation(cloudInformation))
    }

    override fun saveCurrentCloudInformation() {
        cachedInformation.add(CloudStatistic(cpuUsage(), usedMemory(), Agent.eventService.registeredAmount(), System.currentTimeMillis()))
    }

}