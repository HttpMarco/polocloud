package dev.httpmarco.polocloud.suite.cluster.grpc;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import io.grpc.stub.StreamObserver;

public class TestServiceImpl extends ClusterSuiteServiceGrpc.ClusterSuiteServiceImplBase {

    @Override
    public void pingSuite(ClusterService.SuitePingRequest request, StreamObserver<ClusterService.SuitePingResponse> responseObserver) {
        ClusterService.SuitePingResponse response = ClusterService.SuitePingResponse.newBuilder()
                .setState(ClusterService.SuiteState.ONLINE) // Enum-Wert setzen
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
