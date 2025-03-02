package dev.httpmarco.polocloud.suite.cluster.grpc;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ClusterGrpcServiceImpl extends ClusterSuiteServiceGrpc.ClusterSuiteServiceImplBase {


    @Override
    public void auth(ClusterService.AuthIdentificationRequest request, StreamObserver<ClusterService.AuthClusterResponse> responseObserver) {
        ClusterService.AuthClusterResponse response = ClusterService.AuthClusterResponse.newBuilder()
                .setState(ClusterService.SuiteState.ONLINE)
                .setSuccess(false)
                .setMessage("Bis hierhin und nicht weiter!")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
