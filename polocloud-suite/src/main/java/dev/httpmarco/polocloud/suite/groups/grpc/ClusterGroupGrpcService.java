package dev.httpmarco.polocloud.suite.groups.grpc;

import dev.httpmarco.polocloud.explanation.Utils;
import dev.httpmarco.polocloud.explanation.group.ClusterGroupServiceGrpc;
import dev.httpmarco.polocloud.explanation.group.ClusterGroupServiceOuterClass;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import io.grpc.stub.StreamObserver;

public final class ClusterGroupGrpcService extends ClusterGroupServiceGrpc.ClusterGroupServiceImplBase {

    @Override
    public void findGroup(ClusterGroupServiceOuterClass.GroupFindRequest request, StreamObserver<Utils.ClusterGroupExplanation> responseObserver) {
        var response = Utils.ClusterGroupExplanation.newBuilder();
        var group = PolocloudSuite.instance().groupProvider().find(request.getName());

        response.setName(group.name());
        response.setPlatform(Utils.SharedPlatform.newBuilder()
                .setName(group.platform().name())
                .setVersion(group.platform().version().toString())
                .setType(group.platform().type().name())
                .build());
        response.setMinMemory(group.minMemory());
        response.setMaxMemory(group.maxMemory());
        response.setMinOnlineServices(group.minOnlineService());
        response.setMaxOnlineServices(group.maxOnlineService());
        response.setPercentageToStartNewService(group.percentageToStartNewService());

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
