package dev.httpmarco.polocloud.agent.groups

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.utils.asStringMap
import dev.httpmarco.polocloud.v1.groups.FindGroupRequest
import dev.httpmarco.polocloud.v1.groups.FindGroupResponse
import dev.httpmarco.polocloud.v1.groups.GroupControllerGrpc
import dev.httpmarco.polocloud.v1.groups.GroupProvider
import dev.httpmarco.polocloud.v1.groups.GroupSnapshot
import io.grpc.stub.StreamObserver

class GroupGrpcService : GroupControllerGrpc.GroupControllerImplBase() {

    override fun find(
        request: FindGroupRequest,
        responseObserver: StreamObserver<FindGroupResponse>
    ) {

        val builder = FindGroupResponse.newBuilder()
        val groupStorage = Agent.runtime.groupStorage()

        val groupsToReturn = if (request.name.isNotEmpty()) {
            groupStorage.item(request.name)?.let { listOf(it) } ?: emptyList()
        } else {
            groupStorage.items()
        }

        for (group in groupsToReturn) {
            val data = group.data
            builder.addGroups(
                GroupSnapshot.newBuilder()
                    .setName(data.name)
                    .setMinimumMemory(data.minMemory)
                    .setMaximumMemory(data.maxMemory)
                    .setPercentageToStartNewService(data.percentageToStartNewService)
                    .putAllProperties(data.properties.asStringMap())
                    .build()
            )
        }

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()

    }
}