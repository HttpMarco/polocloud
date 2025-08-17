package dev.httpmarco.polocloud.agent.stats

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.stats.GetStatsRequest
import dev.httpmarco.polocloud.v1.stats.GetStatsResponse
import dev.httpmarco.polocloud.v1.stats.StatsControllerGrpc
import io.grpc.stub.StreamObserver

class StatsGrpcService : StatsControllerGrpc.StatsControllerImplBase() {

    override fun get(request: GetStatsRequest, responseObserver: StreamObserver<GetStatsResponse>) {
        val builder = GetStatsResponse.newBuilder()
        val statsStorage = Agent.statsStorage;

        builder.setStats(statsStorage.get().toSnapshot())
        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

}