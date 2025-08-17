package dev.httpmarco.polocloud.agent.platform

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.platform.PlatformControllerGrpc
import dev.httpmarco.polocloud.v1.platform.PlatformFindRequest
import dev.httpmarco.polocloud.v1.platform.PlatformFindResponse
import io.grpc.stub.StreamObserver

class PlatformGrpcService : PlatformControllerGrpc.PlatformControllerImplBase() {

    override fun find(request: PlatformFindRequest, responseObserver: StreamObserver<PlatformFindResponse>) {
        val builder = PlatformFindResponse.newBuilder()
        val platformStorage = Agent.platformStorage

        if(request.hasName()) {
            builder.addPlatforms(platformStorage.find(request.name)?.toSnapshot())
        } else if(request.hasType()) {
            builder.addAllPlatforms(platformStorage.find(request.type).map { it.toSnapshot() })
        } else {
            builder.addAllPlatforms(platformStorage.findAll().map { it.toSnapshot() })
        }

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

}