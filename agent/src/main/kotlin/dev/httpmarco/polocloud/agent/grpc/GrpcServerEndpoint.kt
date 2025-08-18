package dev.httpmarco.polocloud.agent.grpc

import dev.httpmarco.polocloud.agent.events.EventGrpcService
import dev.httpmarco.polocloud.agent.groups.GroupGrpcService
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.player.PlayerGrpcService
import dev.httpmarco.polocloud.agent.services.ServiceGrpcService
import dev.httpmarco.polocloud.agent.information.CloudInformationGrpcService
import dev.httpmarco.polocloud.agent.logging.LoggerGrpcService
import dev.httpmarco.polocloud.agent.platform.PlatformGrpcService
import io.grpc.Server
import io.grpc.ServerBuilder

class GrpcServerEndpoint {

    private lateinit var server: Server

    fun connect(port: Int) {
        this.server = ServerBuilder.forPort(port)
            .addService(EventGrpcService())
            .addService(GroupGrpcService())
            .addService(ServiceGrpcService())
            .addService(PlayerGrpcService())
            .addService(CloudInformationGrpcService())
            .addService(LoggerGrpcService())
            .addService(PlatformGrpcService())
            .build()
        this.server.start()
        i18n.info("agent.starting.grpc.successful", port)
    }

    fun close() {
        if (::server.isInitialized) {
            server.shutdown()
        }
    }
}