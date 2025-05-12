package dev.httpmarco.polocloud.suite.events.grpc;

import dev.httpmarco.polocloud.explanation.event.EventServiceGrpc;
import dev.httpmarco.polocloud.explanation.event.EventServiceOuterClass;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
public final class EventGrpcService extends EventServiceGrpc.EventServiceImplBase {

    @Override
    public void subscribe(EventServiceOuterClass.EventRegisterRequest request, StreamObserver<EventServiceOuterClass.EventRegisterResponse> responseObserver) {

        log.debug("Received subscription request for event: {} on instance {}", request.getEventClass(), request.getServiceId());

        var service = PolocloudSuite.instance().serviceProvider().find(UUID.fromString(request.getServiceId()));

        if (service == null) {
            log.error("Service with id {} not found", request.getServiceId());
            responseObserver.onError(new RuntimeException("Service not found"));
            return;
        }

        PolocloudSuite.instance().eventProvider().subscribePool().subscribe(service, request.getEventClass());
        responseObserver.onNext(EventServiceOuterClass.EventRegisterResponse.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();

    }
}
