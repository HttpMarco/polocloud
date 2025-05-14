package dev.httpmarco.polocloud.suite.services.grpc;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.explanation.Utils;
import dev.httpmarco.polocloud.explanation.group.ClusterServerServiceGrpc;
import dev.httpmarco.polocloud.explanation.group.ClusterServerServiceOuterClass;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
public final class ClusterServiceGrpcService extends ClusterServerServiceGrpc.ClusterServerServiceImplBase {

    @Override
    public void serviceSayOnline(ClusterServerServiceOuterClass.ServiceSayOnlineRequest request, StreamObserver<ClusterServerServiceOuterClass.ServiceSayOnlineResponse> responseObserver) {

        var service = PolocloudSuite.instance().serviceProvider().find(UUID.fromString(request.getServiceId()));

        if (service == null) {
            log.error("Service {} not found", request.getServiceId());
            responseObserver.onError(new RuntimeException("Service not found"));
            return;
        }

        if (service instanceof ClusterLocalServiceImpl localService) {
            localService.channel(ManagedChannelBuilder.forAddress("127.0.0.1", localService.port() + 1).keepAliveWithoutCalls(true).usePlaintext().build());

            responseObserver.onNext(ClusterServerServiceOuterClass.ServiceSayOnlineResponse.newBuilder().build());
            responseObserver.onCompleted();
            return;
        }

        log.error("Service {} is not a local service", request.getServiceId());
        responseObserver.onError(new RuntimeException("Service is not a local service"));
        responseObserver.onCompleted();
    }


    @Override
    public void findAllGroup(ClusterServerServiceOuterClass.FindAllServiceRequest request, StreamObserver<ClusterServerServiceOuterClass.FindAllServiceResponse> responseObserver) {
        var builder = ClusterServerServiceOuterClass.FindAllServiceResponse.newBuilder();

        for (var service : PolocloudSuite.instance().serviceProvider().findAll()) {

            var group = service.group();
            var groupResponse = Utils.ClusterGroupExplanation.newBuilder();

            System.out.println(group.name());
            groupResponse.setName(group.name());
            groupResponse.setPlatform(Utils.SharedPlatform.newBuilder()
                    .setName(group.platform().name())
                    .setVersion(group.platform().version().toString())
                    .setType(group.platform().type().name())
                    .build());
            groupResponse.setMinMemory(group.minMemory());
            groupResponse.setMaxMemory(group.maxMemory());
            groupResponse.setMinOnlineServices(group.minOnlineService());
            groupResponse.setMaxOnlineServices(group.maxOnlineService());
            groupResponse.setPercentageToStartNewService(group.percentageToStartNewService());

            builder.addServices(Utils.ClusterService.newBuilder()
                    .setServiceId(service.uniqueId().toString())
                    .setServiceindex(service.id())
                    .setPort(service.port())
                    .setHostname(service.hostname())
                    .setGroupExplanation(groupResponse.build())
                    .build());
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }
}
