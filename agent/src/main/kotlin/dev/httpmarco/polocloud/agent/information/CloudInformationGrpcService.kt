package dev.httpmarco.polocloud.agent.information

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.information.CloudInformationControllerGrpc
import dev.httpmarco.polocloud.v1.information.GetCloudInformationRequest
import dev.httpmarco.polocloud.v1.information.GetCloudInformationResponse
import io.grpc.stub.StreamObserver

class CloudInformationGrpcService : CloudInformationControllerGrpc.CloudInformationControllerImplBase() {

    override fun get(request: GetCloudInformationRequest, responseObserver: StreamObserver<GetCloudInformationResponse>) {
        val builder = GetCloudInformationResponse.newBuilder()
        val cloudInformationStorage = Agent.cloudInformationStorage;

        builder.setInformation(cloudInformationStorage.get().toSnapshot())
        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

}