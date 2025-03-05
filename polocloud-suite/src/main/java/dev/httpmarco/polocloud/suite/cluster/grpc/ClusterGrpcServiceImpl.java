package dev.httpmarco.polocloud.suite.cluster.grpc;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import dev.httpmarco.polocloud.suite.cluster.suits.ExternalSuite;
import dev.httpmarco.polocloud.suite.configuration.ClusterConfig;
import io.grpc.stub.StreamObserver;

public final class ClusterGrpcServiceImpl extends ClusterSuiteServiceGrpc.ClusterSuiteServiceImplBase {

    @Override
    public void attachSuite(ClusterService.ClusterSuiteAttachRequest request, StreamObserver<ClusterService.ClusterSuiteAttachResponse> responseObserver) {
        var response = ClusterService.ClusterSuiteAttachResponse.newBuilder().setSuccess(true);
        var clusterConfig = PolocloudSuite.instance().config().cluster();
        var clusterProvider = PolocloudSuite.instance().clusterProvider();

        // check if the suite is already attached to a cluster
        if (clusterConfig.clusterToken() != null) {
            response = response.setSuccess(false).setMessage("This suite is already attached to a cluster!");

            // check if the security private key is correct
        } else {
            if (!clusterProvider.local().data().privateKey().equals(request.getSuitePrivateKey())) {
                response = response.setSuccess(false).setMessage("The security private key is not correct!");

                // we only choose a clean suite
            } else if (!clusterConfig.externalSuites().isEmpty()) {
                response = response.setSuccess(false).setMessage("The external suite has local external suite entries! Please remove them first and reboot the external suite!");

                // block the same suite id
            } else if (clusterConfig.localSuite().id().equals(request.getBindSuiteId())) {
                response = response.setSuccess(false).setMessage("The suite id match with the local suite id!");

                // check the same suite id
            } else if (!clusterConfig.localSuite().id().equals(request.getSuiteId())) {
                response = response.setSuccess(false).setMessage("The suite ids not match!");
            } else {
                var bindSuite = new ExternalSuite(new SuiteData(request.getBindSuiteId(), request.getBindSuiteHostname(), request.getBindSuitePort()));

                if (!bindSuite.available()) {
                    response = response.setSuccess(false).setMessage("The bind suite is not available!");
                }

                // todo attach this as the first external suite
                for (SuiteData registeredSuite : bindSuite.registeredSuites()) {
                    var newExternalSuite = new ExternalSuite(registeredSuite);

                    clusterProvider.suites().add(newExternalSuite);
                    clusterConfig.externalSuites().add(newExternalSuite.data());
                }
                // -> request all others

                clusterConfig.clusterToken(request.getClusterToken());

                clusterProvider.suites().add(bindSuite);
                clusterConfig.externalSuites().add(bindSuite.data());
                PolocloudSuite.instance().config().update();
            }
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void findAllSuite(ClusterService.EmptyDummy request, StreamObserver<ClusterService.ClusterSuitesResponse> responseObserver) {
        var response = ClusterService.ClusterSuitesResponse.newBuilder();

        for (var suite : PolocloudSuite.instance().clusterProvider().suites()) {
            response.addSuites(ClusterService.ClusterExternalSuite.newBuilder()
                    .setId(suite.data().id())
                    .setHostname(suite.data().hostname())
                    .setPort(suite.data().port())
                    .build());
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
