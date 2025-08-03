package dev.httpmarco.polocloud.agent.groups

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.groups.FindGroupRequest
import dev.httpmarco.polocloud.v1.groups.FindGroupResponse
import dev.httpmarco.polocloud.v1.groups.GroupControllerGrpc
import io.grpc.stub.StreamObserver

class GroupGrpcService : GroupControllerGrpc.GroupControllerImplBase() {

    override fun find(
        request: FindGroupRequest,
        responseObserver: StreamObserver<FindGroupResponse>
    ) {

        val builder = FindGroupResponse.newBuilder()
        val groupStorage = Agent.runtime.groupStorage()

        val groupsToReturn = if (request.name.isNotEmpty()) {
            groupStorage.find(request.name)?.let { listOf(it) } ?: emptyList()
        } else {
            groupStorage.findAll()
        }

        for (group in groupsToReturn) {
            builder.addGroups(group.toSnapshot())
        }

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()

    }
}