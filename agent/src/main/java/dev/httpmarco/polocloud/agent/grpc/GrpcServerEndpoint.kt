package dev.httpmarco.polocloud.agent.grpc

import dev.httpmarco.polocloud.agent.events.EventGrpcService
import dev.httpmarco.polocloud.agent.groups.GroupGrpcService
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.services.ServiceGrpcService
import io.grpc.Server
import io.grpc.ServerBuilder

class GrpcServerEndpoint {

    private lateinit var server: Server

    fun connect() {
        this.server = ServerBuilder.forPort(8932)
            .addService(EventGrpcService())
            .addService(GroupGrpcService())
            .addService(ServiceGrpcService())
            .build()
        this.server.start()
        logger.info("Successfully started gRPC server on port 8932")
    }

    fun close() {
        server.shutdown()
    }
}