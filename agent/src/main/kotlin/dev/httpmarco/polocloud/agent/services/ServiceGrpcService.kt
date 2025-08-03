package dev.httpmarco.polocloud.agent.services

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceControllerGrpc
import dev.httpmarco.polocloud.v1.services.ServiceFindRequest
import dev.httpmarco.polocloud.v1.services.ServiceFindResponse
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import io.grpc.stub.StreamObserver

class ServiceGrpcService : ServiceControllerGrpc.ServiceControllerImplBase() {

    override fun find(request: ServiceFindRequest?, responseObserver: StreamObserver<ServiceFindResponse>) {
        val serviceStorage = Agent.runtime.serviceStorage();
        val builder = ServiceFindResponse.newBuilder()

        serviceStorage.findAll().forEach {
            builder.addServices(it.toSnapshot())
        }
        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }
}