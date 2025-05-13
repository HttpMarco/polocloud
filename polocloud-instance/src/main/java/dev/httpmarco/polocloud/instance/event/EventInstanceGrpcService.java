package dev.httpmarco.polocloud.instance.event;

import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.explanation.event.EventServiceGrpc;
import dev.httpmarco.polocloud.explanation.event.EventServiceOuterClass;
import dev.httpmarco.polocloud.instance.PolocloudInstance;
import dev.httpmarco.polocloud.instance.utils.GsonInstance;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;

public final class EventInstanceGrpcService extends EventServiceGrpc.EventServiceImplBase {

    @SneakyThrows
    @Override
    public void call(EventServiceOuterClass.Event request, StreamObserver<EventServiceOuterClass.EventResponse> responseObserver) {

        Event event = (Event) GsonInstance.DEFAULT.fromJson(request.getContent(), Class.forName(request.getEventClass()));

        PolocloudInstance.instance().eventProvider().call(event);

        responseObserver.onNext(EventServiceOuterClass.EventResponse.newBuilder().build());
        responseObserver.onCompleted();


    }
}
