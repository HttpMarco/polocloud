package dev.httpmarco.polocloud.agent.information

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.information.CloudInformationControllerGrpc
import dev.httpmarco.polocloud.v1.information.CloudInformationFindRequest
import dev.httpmarco.polocloud.v1.information.CloudInformationFindResponse
import io.grpc.stub.StreamObserver

class CloudInformationGrpcService : CloudInformationControllerGrpc.CloudInformationControllerImplBase() {

    override fun find(request: CloudInformationFindRequest, responseObserver: StreamObserver<CloudInformationFindResponse>) {
        val builder = CloudInformationFindResponse.newBuilder()
        val cloudInformationStorage = Agent.cloudInformationStorage;

        if(!request.hasFrom()) {
            if(!request.hasTo()) {
                builder.addInformation(cloudInformationStorage.find().toSnapshot())
            } else {
                builder.addAllInformation(cloudInformationStorage.find(0, request.to).map { it.toSnapshot() })
            }
        } else {
            if(request.hasTo()) {
                builder.addAllInformation(cloudInformationStorage.find(request.from, request.to).map { it.toSnapshot() })
            } else {
                builder.addAllInformation(cloudInformationStorage.find(request.from, System.currentTimeMillis()).map { it.toSnapshot() })
            }
        }

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

}