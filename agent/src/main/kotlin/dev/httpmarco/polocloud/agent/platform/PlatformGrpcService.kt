package dev.httpmarco.polocloud.agent.platform

import dev.httpmarco.polocloud.platforms.PlatformPool
import dev.httpmarco.polocloud.shared.platform.Platform
import dev.httpmarco.polocloud.shared.platform.PlatformVersion
import dev.httpmarco.polocloud.v1.platform.PlatformControllerGrpc
import dev.httpmarco.polocloud.v1.platform.PlatformFindRequest
import dev.httpmarco.polocloud.v1.platform.PlatformFindResponse
import io.grpc.stub.StreamObserver

class PlatformGrpcService : PlatformControllerGrpc.PlatformControllerImplBase() {

    override fun find(request: PlatformFindRequest, responseObserver: StreamObserver<PlatformFindResponse>) {
        val builder = PlatformFindResponse.newBuilder()
        val platformPool = PlatformPool.platforms()

        if(request.hasName()) {
            builder.addAllPlatforms(
                platformPool
                    .filter { it.name.lowercase() == request.name.lowercase() }
                    .map { Platform(it.name, it.type, it.versions.map { pv -> PlatformVersion(pv.version) }).toSnapshot() }
            )
        } else if(request.hasType()) {
            builder.addAllPlatforms(
                platformPool
                    .filter { it.type == request.type }
                    .map { Platform(it.name, it.type, it.versions.map { pv -> PlatformVersion(pv.version) }).toSnapshot() }
            )
        } else {
            builder.addAllPlatforms(
                platformPool
                    .map { Platform(it.name, it.type, it.versions.map { pv -> PlatformVersion(pv.version) }).toSnapshot() }
            )
        }

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

}