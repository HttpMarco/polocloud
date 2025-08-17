package dev.httpmarco.polocloud.agent.stats

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.common.os.cpuUsage
import dev.httpmarco.polocloud.common.os.usedMemory
import dev.httpmarco.polocloud.shared.stats.Stats
import dev.httpmarco.polocloud.v1.stats.GetStatsRequest
import dev.httpmarco.polocloud.v1.stats.GetStatsResponse
import dev.httpmarco.polocloud.v1.stats.StatsControllerGrpc
import io.grpc.stub.StreamObserver

class StatsGrpcService : StatsControllerGrpc.StatsControllerImplBase() {

    override fun get(request: GetStatsRequest, responseObserver: StreamObserver<GetStatsResponse>) {
        val builder = GetStatsResponse.newBuilder()

        val stats = Stats(
            Agent.runtime.started(),
            cpuUsage(),
            usedMemory()
        )
        builder.setStats(stats.toSnapshot())
        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

}