package dev.httpmarco.polocloud.agent.events

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.shared.events.ServiceOnlineEvent
import dev.httpmarco.polocloud.v1.proto.EventProviderGrpc
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass
import io.grpc.stub.StreamObserver

class EventGrpcService : EventProviderGrpc.EventProviderImplBase() {

    override fun subscribe(request: EventProviderOuterClass.EventSubscribeRequest, responseObserver: StreamObserver<EventProviderOuterClass.EventContext>) {
        Agent.instance.eventService.attach(request.eventName, responseObserver)
        println("Empfangenes Event: ${request.eventName}")
        Agent.instance.eventService.call(ServiceOnlineEvent())
    }
}