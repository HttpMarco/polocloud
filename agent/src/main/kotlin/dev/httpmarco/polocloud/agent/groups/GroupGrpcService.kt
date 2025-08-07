package dev.httpmarco.polocloud.agent.groups

import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import dev.httpmarco.polocloud.v1.groups.FindGroupRequest
import dev.httpmarco.polocloud.v1.groups.FindGroupResponse
import dev.httpmarco.polocloud.v1.groups.GroupControllerGrpc
import dev.httpmarco.polocloud.v1.groups.GroupCreateRequest
import dev.httpmarco.polocloud.v1.groups.GroupCreateResponse
import dev.httpmarco.polocloud.v1.groups.GroupDeleteRequest
import dev.httpmarco.polocloud.v1.groups.GroupDeleteResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver

class GroupGrpcService : GroupControllerGrpc.GroupControllerImplBase() {

    override fun find(request: FindGroupRequest, responseObserver: StreamObserver<FindGroupResponse>) {

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

    override fun create(request: GroupCreateRequest, responseObserver: StreamObserver<GroupCreateResponse>) {
        val groupStorage = Agent.runtime.groupStorage()
        val builder = GroupCreateResponse.newBuilder()

        if (groupStorage.find(request.name) != null) {
            responseObserver.onError(StatusRuntimeException(Status.ALREADY_EXISTS))
            return
        }

        val properties = HashMap<String, JsonPrimitive>()
        request.propertiesMap.forEach { t, u -> {
            properties[t] = JsonPrimitive(u)
        } }

        val group = AbstractGroup(
            request.name,
            request.minimumMemory,
            request.maximumMemory,
            request.minimumOnline,
            request.maximumOnline,
            request.percentageToStartNewService,
            PlatformIndex(request.platform.name, request.platform.version),
            request.templatesList,
            properties
        )

        Agent.runtime.groupStorage().publish(group)
        builder.setGroup(group.toSnapshot())
        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()

    }

    override fun delete(request: GroupDeleteRequest, responseObserver: StreamObserver<GroupDeleteResponse>) {
        val groupStorage = Agent.runtime.groupStorage()
        val builder = GroupDeleteResponse.newBuilder()

        val group = groupStorage.find(request.name)
        if (group == null) {
            responseObserver.onError(StatusRuntimeException(Status.NOT_FOUND))
            return
        }

        Agent.runtime.serviceStorage().findByGroup(group).forEach { it.shutdown() }
        Agent.runtime.groupStorage().delete(group.name)
        builder.setGroup(group.toSnapshot())
        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

}