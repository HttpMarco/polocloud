package dev.httpmarco.polocloud.agent.groups

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.proto.GroupProvider
import dev.httpmarco.polocloud.v1.proto.GroupProviderHandlerGrpc
import io.grpc.stub.StreamObserver

class GroupGrpcService : GroupProviderHandlerGrpc.GroupProviderHandlerImplBase() {

    override fun find(
        request: GroupProvider.FindRequest,
        responseObserver: StreamObserver<GroupProvider.FindResponse>
    ) {

        val builder = GroupProvider.FindResponse.newBuilder()
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