package dev.httpmarco.polocloud.agent.stats

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.common.os.cpuUsage
import dev.httpmarco.polocloud.common.os.usedMemory
import dev.httpmarco.polocloud.v1.groups.GetStatsRequest
import dev.httpmarco.polocloud.v1.groups.GetStatsResponse
import dev.httpmarco.polocloud.v1.groups.StatsControllerGrpc
import dev.httpmarco.polocloud.v1.groups.StatsSnapshot
import io.grpc.stub.StreamObserver

class StatsGrpcService : StatsControllerGrpc.StatsControllerImplBase() {

    override fun get(request: GetStatsRequest, responseObserver: StreamObserver<GetStatsResponse>) {
        val builder = GetStatsResponse.newBuilder()

        val started = Agent.runtime.started()
        val usedMemory = usedMemory()
        val cpuUsage = cpuUsage()

        val stats = StatsSnapshot.newBuilder()
            .setStarted(started)
            .setCpuUsage(cpuUsage)
            .setUsedMemory(usedMemory)
        builder.setStats(stats)
        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

}