package dev.httpmarco.polocloud.suite.cluster.grpc;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import dev.httpmarco.polocloud.suite.cluster.global.GlobalCluster;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ClusterSuiteGrpcHandler extends ClusterSuiteServiceGrpc.ClusterSuiteServiceImplBase {

    private static final Logger log = LogManager.getLogger(ClusterSuiteGrpcHandler.class);

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
                    response.setToken(globalConfig.token());
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

    @Override
    public void requestState(ClusterService.EmptyCall request, StreamObserver<ClusterService.ClusterSuiteStateResponse> responseObserver) {
        var response = ClusterService.ClusterSuiteStateResponse.newBuilder();
        if (PolocloudSuite.instance().cluster() instanceof GlobalCluster globalCluster) {
            response.setState(globalCluster.state());
        } else {
            // this should never happen, but if we are here, the cluster is not configured correctly
            log.warn("Cluster is not configured correctly! The bound suite is local, but we need a global cluster!");
            response.setState(ClusterService.State.INVALID);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void drainCluster(ClusterService.SuiteDrainRequest request, StreamObserver<ClusterService.EmptyCall> responseObserver) {
        if (PolocloudSuite.instance().cluster() instanceof GlobalCluster globalCluster) {
            var externalSuite = globalCluster.find(request.getId());
            if (externalSuite == null) {
                log.error("The suite {} could not be found in the cluster! But external tries to drain it!", request.getId());
            } else {
                externalSuite.close();
                globalCluster.suites().remove(externalSuite);
                log.info("The suite {} has been drained from the cluster!", externalSuite.id());
            }
        } else {
            log.warn("Cluster is not configured correctly! The bound suite is local, but we need a global cluster!");
        }


        responseObserver.onNext(ClusterService.EmptyCall.newBuilder().build());
        responseObserver.onCompleted();
    }
}
