package dev.httpmarco.polocloud.agent.services

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.ServiceControllerGrpc
import dev.httpmarco.polocloud.v1.ServiceFindRequest
import dev.httpmarco.polocloud.v1.ServiceFindResponse
import dev.httpmarco.polocloud.v1.ServiceSnapshot
import io.grpc.stub.StreamObserver

class ServiceGrpcService : ServiceControllerGrpc.ServiceControllerImplBase() {

    override fun find(request: ServiceFindRequest?, responseObserver: StreamObserver<ServiceFindResponse>) {
        val serviceStorage = Agent.instance.runtime.serviceStorage();
        val builder = ServiceFindResponse.newBuilder()

        serviceStorage.items().forEach {
            builder.addServices(
                ServiceSnapshot.newBuilder()
                    .setGroupName(it.group.data.name)
                    .setId(it.id)
                    .setServerType(GroupType.valueOf(it.group.platform().type.name))
                    .setHostname(it.hostname)
                    .setPort(it.port)
                    .setMaxPlayers(it.maxPlayerCount.toLong())
                    .setPlayerCount(it.playerCount.toLong())
                    .build()
            )
        }
        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }
}