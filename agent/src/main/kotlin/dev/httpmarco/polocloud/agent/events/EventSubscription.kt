package dev.httpmarco.polocloud.agent.events

import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass
import io.grpc.stub.ServerCallStreamObserver

class EventSubscription(val service: Service, val sub : ServerCallStreamObserver<EventProviderOuterClass.EventContext>) {

}