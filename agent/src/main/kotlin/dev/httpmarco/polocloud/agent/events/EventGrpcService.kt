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
        println("Calling event: ${request.eventName} with data: ${request.eventData}")
        runCatching {
            val fqcn = "dev.httpmarco.polocloud.shared.events.definitions.${request.eventName}"
            val eventClass = Class.forName(fqcn)
            val eventObj = Agent.eventService.gsonSerializer
                .fromJson(request.eventData, eventClass) as Event

            println("About to call Agent.eventService.call(...) with ${eventObj.javaClass.name}")
            Agent.eventService.call(eventObj)

            EventProviderOuterClass.CallEventResponse.newBuilder()
                .setSuccess(true)
                .build()
        }.onSuccess { response ->
            responseObserver.onNext(response)
        }.onFailure { e ->
            println(e)

            responseObserver.onNext(
                EventProviderOuterClass.CallEventResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(e.message ?: "Unknown error")
                    .build()
            )
        }
    }
}