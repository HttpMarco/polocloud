package dev.httpmarco.polocloud.agent.groups

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.proto.GroupControllerGrpc
import dev.httpmarco.polocloud.v1.proto.GroupProvider
import io.grpc.stub.StreamObserver

class GroupGrpcService : GroupControllerGrpc.GroupControllerImplBase() {

    override fun find(
        request: GroupProvider.FindGroupRequest,
        responseObserver: StreamObserver<GroupProvider.FindGroupResponse>
    ) {

        val builder = GroupProvider.FindGroupResponse.newBuilder()
        val groupStorage = Agent.instance.runtime.groupStorage()

        if (request.name.isNotEmpty()) {
            val group = groupStorage.item(request.name)

            if (group != null) {
                builder.addGroups(GroupProvider.GroupSnapshot.newBuilder().setName(group.data.name).build())
            }
        } else {
            groupStorage.items().forEach {
                builder.addGroups(GroupProvider.GroupSnapshot.newBuilder().setName(it.data.name).build())
            }
        }

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()

    }
}