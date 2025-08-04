package dev.httpmarco.polocloud.bridges.gate

import build.buf.gen.minekube.gate.v1.GateServiceGrpcKt
import build.buf.gen.minekube.gate.v1.Server
import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import io.grpc.ManagedChannelBuilder

class GateBridge(hostname: String, port: Int) : BridgeInstance<Server>() {

    val channel = ManagedChannelBuilder
        .forAddress("localhost", 8080)
        .usePlaintext()
        .build()

    override fun generateInfo(
        name: String,
        hostname: String,
        port: Int
    ): Server {
        return TODO()
    }

    override fun registerService(identifier: Server, fallback: Boolean) {
        val stub = GateServiceGrpcKt.GateServiceCoroutineStub(channel)
    }


    override fun unregisterService(identifier: Server) {
        TODO("Not yet implemented")
    }

    override fun findInfo(name: String): Server? {
        return null
    }
}