package dev.httpmarco.polocloud.sdk.java.events;

import com.google.gson.GsonBuilder;
import dev.httpmarco.polocloud.sdk.java.Polocloud;
import dev.httpmarco.polocloud.shared.events.Event;
import dev.httpmarco.polocloud.shared.events.SharedEventProvider;
import dev.httpmarco.polocloud.v1.proto.EventProviderGrpc;
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;

public final class EventProvider extends SharedEventProvider {

    private final EventProviderGrpc.EventProviderStub eventStub;

    public EventProvider(ManagedChannel channel) {
        this.eventStub = EventProviderGrpc.newStub(channel);
    }

    @Override
    public void call(@NotNull Event event) {

    }

    @Override
    public <T extends Event> void subscribe(@NotNull KClass<T> eventType, @NotNull Function1<? super T, ?> result) {
        EventProviderOuterClass.EventSubscribeRequest request =
                EventProviderOuterClass.EventSubscribeRequest.newBuilder()
                        .setServiceName(Polocloud.instance().selfServiceName())
                        .setEventName(eventType.getSimpleName())
                        .build();

        eventStub.subscribe(request, new StreamObserver<>() {
            @Override
            public void onNext(EventProviderOuterClass.EventContext context) {
                result.invoke(getGsonSerilaizer().fromJson(context.getEventData(), JvmClassMappingKt.getJavaClass(eventType)));
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error while subscribing to event: " + t.getMessage());
                t.printStackTrace(System.err);
            }

            @Override
            public void onCompleted() {
                // No action needed on completion
            }
        });
    }
}
