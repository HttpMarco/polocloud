package dev.httpmarco.polocloud.agent.events

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.proto.EventProviderGrpc
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass
import io.grpc.stub.StreamObserver

class EventGrpcService : EventProviderGrpc.EventProviderImplBase() {

    override fun subscribe(
        request: EventProviderOuterClass.EventSubscribeRequest,
        responseObserver: StreamObserver<EventProviderOuterClass.EventContext>
    ) {
        Agent.eventService.attach(request.eventName, request.serviceName, responseObserver)
    }
}