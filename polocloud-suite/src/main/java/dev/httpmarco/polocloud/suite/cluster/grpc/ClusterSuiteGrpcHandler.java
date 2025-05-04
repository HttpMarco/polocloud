package dev.httpmarco.polocloud.suite.cluster.grpc;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.global.GlobalCluster;
import dev.httpmarco.polocloud.suite.cluster.global.suites.ExternalSuite;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ClusterSuiteGrpcHandler extends ClusterSuiteServiceGrpc.ClusterSuiteServiceImplBase {

    private static final Logger log = LogManager.getLogger(ClusterSuiteGrpcHandler.class);

    @Override
    public void attachSuite(ClusterService.ClusterSuiteAttachRequest request, StreamObserver<ClusterService.ClusterSuiteAttachResponse> responseObserver) {
        var response = ClusterService.ClusterSuiteAttachResponse.newBuilder().setSuccess(false);
        var localConfig = PolocloudSuite.instance().config().cluster();

        //todo
        if (localConfig instanceof ClusterGlobalConfig globalConfig) {
            if (!globalConfig.privateKey().equals(request.getSuitePrivateKey())) {
                response.setMessage(PolocloudSuite.instance().translation().get("cluster.grpc.handler.attachSuite.invalidPrivateKey"));
            } else {
                if (globalConfig.id().equals(request.getSuiteId())) {
                    response.setSuccess(true);
                    response.setToken(globalConfig.token());
                } else {
                    response.setMessage(PolocloudSuite.instance().translation().get("cluster.grpc.handler.invalidToken"));
                }
            }

        } else {
            response.setMessage(PolocloudSuite.instance().translation().get("cluster.grpc.handler.clusterNotConfigured"));
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void requestState(ClusterService.EmptyCall request, StreamObserver<ClusterService.ClusterSuiteStateResponse> responseObserver) {
        var response = ClusterService.ClusterSuiteStateResponse.newBuilder();

        //todo
        if (PolocloudSuite.instance().cluster() instanceof GlobalCluster globalCluster) {
            response.setState(globalCluster.state());
        } else {
            // this should never happen, but if we are here, the cluster is not configured correctly
            log.warn(PolocloudSuite.instance().translation().get("cluster.grpc.handler.clusterNotFound"));
            response.setState(ClusterService.State.INVALID);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void drainCluster(ClusterService.SuiteDrainRequest request, StreamObserver<ClusterService.EmptyCall> responseObserver) {
        //todo
        if (PolocloudSuite.instance().cluster() instanceof GlobalCluster globalCluster) {
            var externalSuite = globalCluster.find(request.getId());
            if (externalSuite == null) {
                log.error(PolocloudSuite.instance().translation().get("cluster.grpc.handler.suiteNotFound", request.getId()));
            } else {
                externalSuite.close();
                globalCluster.suites().remove(externalSuite);
                log.info(PolocloudSuite.instance().translation().get("cluster.grpc.handler.suiteDrained", externalSuite.id()));
            }
        } else {
            log.warn(PolocloudSuite.instance().translation().get("cluster.grpc.handler.clusterNotFound"));
        }


        responseObserver.onNext(ClusterService.EmptyCall.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void runtimeHandshake(ClusterService.SuiteRuntimeHandShakeRequest request, StreamObserver<ClusterService.EmptyCall> responseObserver) {
        var newSuite = new ExternalSuite(new ClusterSuiteData(request.getId(), request.getHostname(), request.getPrivateKey(), request.getPort()));

        if (PolocloudSuite.instance().cluster() instanceof GlobalCluster globalCluster) {
            if (globalCluster.find(request.getId()) != null) {
                log.error(PolocloudSuite.instance().translation().get("cluster.grpc.handler.suiteAlreadyRegistered", request.getId()));
            } else {
                // welcome new suite
                globalCluster.suites().add(newSuite);
                newSuite.state(newSuite.available() ? newSuite.clusterStub().requestState(ClusterService.EmptyCall.newBuilder().build()).getState() : ClusterService.State.OFFLINE);

                if (newSuite.state() == ClusterService.State.AVAILABLE) {
                    log.info(PolocloudSuite.instance().translation().get("cluster.grpc.handler.suiteOnline", newSuite.id()));
                }
                // the suite status task will handle the rest
            }

        }

        responseObserver.onNext(ClusterService.EmptyCall.newBuilder().build());
        responseObserver.onCompleted();
    }
}
