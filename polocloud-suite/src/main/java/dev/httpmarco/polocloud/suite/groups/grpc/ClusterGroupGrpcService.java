package dev.httpmarco.polocloud.suite.groups.grpc;

import dev.httpmarco.polocloud.explanation.group.ClusterGroupExplanationOuterClass;
import dev.httpmarco.polocloud.explanation.group.ClusterGroupServiceGrpc;
import dev.httpmarco.polocloud.explanation.group.ClusterGroupServiceOuterClass;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import io.grpc.stub.StreamObserver;

public final class ClusterGroupGrpcService extends ClusterGroupServiceGrpc.ClusterGroupServiceImplBase {

    @Override
    public void findGroup(ClusterGroupServiceOuterClass.GroupFindRequest request, StreamObserver<ClusterGroupExplanationOuterClass.ClusterGroupExplanation> responseObserver) {
        var response = ClusterGroupExplanationOuterClass.ClusterGroupExplanation.newBuilder();
        var group = PolocloudSuite.instance().groupProvider().find(request.getName());

        response.setName(group.name());

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
