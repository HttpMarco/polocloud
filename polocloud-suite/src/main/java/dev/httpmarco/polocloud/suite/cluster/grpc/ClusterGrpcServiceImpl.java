package dev.httpmarco.polocloud.suite.cluster.grpc;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import dev.httpmarco.polocloud.suite.cluster.suits.ExternalSuite;
import dev.httpmarco.polocloud.suite.configuration.ClusterConfig;
import io.grpc.stub.StreamObserver;

public class ClusterGrpcServiceImpl extends ClusterSuiteServiceGrpc.ClusterSuiteServiceImplBase {


    @Override
    public void attachSuite(ClusterService.CLusterSuiteAttachRequest request, StreamObserver<ClusterService.AuthClusterResponse> responseObserver) {
        var response = ClusterService.AuthClusterResponse.newBuilder().setSuccess(true);
        var clusterConfig = PolocloudSuite.instance().config().cluster();

        // check if the suite is already attached to a cluster
        if (clusterConfig.clusterToken() != null) {
            response = response.setSuccess(false).setMessage("This suite is already attached to a cluster!");

            // check if the security private key is correct
        } else if (!PolocloudSuite.instance().clusterProvider().local().data().privateKey().equals(request.getSuitePrivateKey())) {
            response = response.setSuccess(false).setMessage("The security private key is not correct!");

            // we only choose a clean suite
        } else if(!clusterConfig.externalSuites().isEmpty()) {
            response = response.setSuccess(false).setMessage("The external suite has local external suite entries! Please remove them first and reboot the external suite!");

            // block the same suite id
        }else if(clusterConfig.localSuite().id().equals(request.getBindSuiteId())) {
            response = response.setSuccess(false).setMessage("The suite id match with the local suite id!");

            // check the same suite id
        } else if (!clusterConfig.localSuite().id().equals(request.getSuiteId())) {
            response = response.setSuccess(false).setMessage("The suite ids not match!");
        } else {
            var bindSuite = new ExternalSuite(new SuiteData(request.getBindSuiteId(), request.getBindSuiteHostname(), request.getBindSuitePort()));

            if (!bindSuite.available()) {
                response = response.setSuccess(false).setMessage("The bind suite is not available!");
            }

            // todo write new cluster id to config
            // todo attach this as the first external suite
            clusterConfig.clusterToken(request.getClusterToken());

            PolocloudSuite.instance().clusterProvider().suites().add(bindSuite);
            clusterConfig.externalSuites().add(bindSuite.data());
            PolocloudSuite.instance().config().update();
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
