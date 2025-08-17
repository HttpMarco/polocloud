package dev.httpmarco.polocloud.agent.information

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.common.os.cpuUsage
import dev.httpmarco.polocloud.common.os.usedMemory
import dev.httpmarco.polocloud.shared.information.AggregateCloudInformation
import dev.httpmarco.polocloud.shared.information.CloudInformation
import dev.httpmarco.polocloud.shared.information.StatAggregate

private val cachedInformation =  mutableListOf<CloudStatistic>()

private val minuteAggregates = mutableMapOf<Long, StatAggregate>()
private val hourAggregates = mutableMapOf<Long, StatAggregate>()
private val dayAggregates = mutableMapOf<Long, StatAggregate>()

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

    override fun findMinutes(from: Long, to: Long): List<AggregateCloudInformation> {
        return minuteAggregates
            .filterKeys { it in (from / 60000)..(to / 60000) }
            .map { (bucket, agg) ->
                AggregateCloudInformation(
                    timestamp = bucket * 60000,
                    avgCpu = if (agg.count > 0) agg.cpuSum / agg.count else 0.0,
                    avgRam = if (agg.count > 0) agg.ramSum / agg.count else 0.0
                )
            }
    }

    override fun findHours(from: Long, to: Long): List<AggregateCloudInformation> {
        return hourAggregates
            .filterKeys { it in (from / 3600000)..(to / 3600000) }
            .map { (bucket, agg) ->
                AggregateCloudInformation(
                    timestamp = bucket * 3600000,
                    avgCpu = if (agg.count > 0) agg.cpuSum / agg.count else 0.0,
                    avgRam = if (agg.count > 0) agg.ramSum / agg.count else 0.0
                )
            }
    }

    override fun findDays(from: Long, to: Long): List<AggregateCloudInformation> {
        return dayAggregates
            .filterKeys { it in (from / 86400000)..(to / 86400000) }
            .map { (bucket, agg) ->
                AggregateCloudInformation(
                    timestamp = bucket * 86400000,
                    avgCpu = if (agg.count > 0) agg.cpuSum / agg.count else 0.0,
                    avgRam = if (agg.count > 0) agg.ramSum / agg.count else 0.0
                )
            }
    }

    override fun findAverage(from: Long, to: Long): AggregateCloudInformation {
        val relevant = cachedInformation.filter { it.timestamp in from..to }
        val agg = StatAggregate()
        relevant.forEach {
            agg.cpuSum += it.cpuUsage
            agg.ramSum += it.usedMemory
            agg.count++
        }
        return AggregateCloudInformation(
            timestamp = from,
            avgCpu = if (agg.count > 0) agg.cpuSum / agg.count else 0.0,
            avgRam = if (agg.count > 0) agg.ramSum / agg.count else 0.0
        )
    }

    override fun cleanup(maxAgeMillis: Long) {
        val cutoff = System.currentTimeMillis() - maxAgeMillis
        cachedInformation.removeIf { it.timestamp < cutoff }

        val minuteCutoff = cutoff / 60000
        minuteAggregates.keys.removeIf { it < minuteCutoff }

        val hourCutoff = cutoff / 3600000
        hourAggregates.keys.removeIf { it < hourCutoff }

        val dayCutoff = cutoff / 86400000
        dayAggregates.keys.removeIf { it < dayCutoff }
    }

    override fun addCloudInformation(cloudInformation: CloudInformation) {
        cachedInformation.add(CloudStatistic.bindCloudInformation(cloudInformation))
    }

    override fun removeCloudInformation(cloudInformation: CloudInformation) {
        cachedInformation.remove(CloudStatistic.bindCloudInformation(cloudInformation))
    }

    override fun saveCurrentCloudInformation() {
        val now = System.currentTimeMillis()
        val cpu = cpuUsage()
        val ram = usedMemory()

        cachedInformation.add(CloudStatistic(cpu, ram, Agent.eventService.registeredAmount(), now))

        val minuteBucket = now / 60000
        val hourBucket = now / 3600000
        val dayBucket = now / 86400000

        updateAggregate(minuteAggregates, minuteBucket, cpu, ram)
        updateAggregate(hourAggregates, hourBucket, cpu, ram)
        updateAggregate(dayAggregates, dayBucket, cpu, ram)
    }


    private fun updateAggregate(
        map: MutableMap<Long, StatAggregate>,
        bucket: Long,
        cpu: Double,
        ram: Double
    ) {
        val agg = map.getOrPut(bucket) { StatAggregate() }
        agg.cpuSum += cpu
        agg.ramSum += ram
        agg.count++
    }

}