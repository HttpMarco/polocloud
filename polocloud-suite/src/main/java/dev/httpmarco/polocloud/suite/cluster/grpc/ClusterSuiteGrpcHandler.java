package dev.httpmarco.polocloud.suite.cluster.grpc;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import io.grpc.stub.StreamObserver;

public final class ClusterSuiteGrpcHandler extends ClusterSuiteServiceGrpc.ClusterSuiteServiceImplBase {

    @Override
    public void attachSuite(ClusterService.ClusterSuiteAttachRequest request, StreamObserver<ClusterService.ClusterSuiteAttachResponse> responseObserver) {
        var response = ClusterService.ClusterSuiteAttachResponse.newBuilder().setSuccess(false);



        // todo

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
