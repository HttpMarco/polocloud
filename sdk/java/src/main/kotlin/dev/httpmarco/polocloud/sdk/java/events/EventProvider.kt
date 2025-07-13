package dev.httpmarco.polocloud.sdk.java.events

import com.google.gson.GsonBuilder
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.v1.proto.EventProviderGrpc
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass
import io.grpc.ManagedChannel
import io.grpc.stub.StreamObserver
import java.util.function.Consumer

class EventProvider(channel: ManagedChannel?) {

    private val eventStub = EventProviderGrpc.newStub(channel)

    fun <T :  Event> subscribe(event: Class<T>, listener: Consumer<T>) {
        val request = EventProviderOuterClass.EventSubscribeRequest.newBuilder()
            .setServiceName(Polocloud.instance().selfServiceName())
            .setEventName(event.simpleName)
            .build()

        eventStub.subscribe(request, object : StreamObserver<EventProviderOuterClass.EventContext> {
            override fun onNext(conetext: EventProviderOuterClass.EventContext) {
                listener.accept(GsonBuilder().create().fromJson(conetext.eventData, event))
            }

            override fun onError(p0: Throwable) {
                System.err.println("Error while subscribing to event: ${p0.message}")
                p0.printStackTrace()
            }

            override fun onCompleted() {
                // No action needed on completion
            }
        })
    }
}