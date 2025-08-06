package dev.httpmarco.polocloud.agent.events

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.v1.proto.EventProviderGrpc
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass
import io.grpc.stub.ServerCallStreamObserver
import io.grpc.stub.StreamObserver

class EventGrpcService : EventProviderGrpc.EventProviderImplBase() {

    override fun subscribe(
        request: EventProviderOuterClass.EventSubscribeRequest,
        responseObserver: StreamObserver<EventProviderOuterClass.EventContext>
    ) {
        Agent.eventService.attach(
            request.eventName,
            request.serviceName,
            responseObserver as ServerCallStreamObserver<EventProviderOuterClass.EventContext>
        )
    }

    override fun call(
        request: EventProviderOuterClass.EventContext,
        responseObserver: StreamObserver<EventProviderOuterClass.CallEventResponse>
    ) {
        val serverObserver = responseObserver as ServerCallStreamObserver<EventProviderOuterClass.CallEventResponse>
        if (serverObserver.isCancelled) {
            return
        }

        val fqcn = "dev.httpmarco.polocloud.shared.events.definitions.${request.eventName}"
        val eventClass = Class.forName(fqcn)
        val eventObj = Agent.eventService.gsonSerializer
            .fromJson(request.eventData, eventClass) as Event

        Agent.eventService.call(eventObj)

        if (!serverObserver.isCancelled) {
            serverObserver.onNext(
                EventProviderOuterClass.CallEventResponse.newBuilder()
                    .setSuccess(true)
                    .build()
            )
        }
    }
}