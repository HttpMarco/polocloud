package dev.httpmarco.polocloud.suite.cluster.grpc;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import io.grpc.stub.StreamObserver;

public final class ClusterSuiteGrpcHandler extends ClusterSuiteServiceGrpc.ClusterSuiteServiceImplBase {

    @Override
    public void attachSuite(ClusterService.ClusterSuiteAttachRequest request, StreamObserver<ClusterService.ClusterSuiteAttachResponse> responseObserver) {
        var response = ClusterService.ClusterSuiteAttachResponse.newBuilder().setSuccess(false);
        var localConfig = PolocloudSuite.instance().config().cluster();

        if (localConfig instanceof ClusterGlobalConfig globalConfig) {
            if (!globalConfig.privateKey().equals(request.getSuitePrivateKey())) {
                response.setMessage("Invalid private key!");
            } else {
                if (globalConfig.id().equals(request.getSuiteId())) {
                    response.setSuccess(true);
                    response.setMessage(globalConfig.token());
                } else {
                    response.setMessage("Invalid token!");
                }
            }

        } else {
            response.setMessage("Cluster is not configured correctly! The binded suite is local");
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
