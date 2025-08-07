package dev.httpmarco.polocloud.sdk.java.groups;

import dev.httpmarco.polocloud.common.future.FutureConverterKt;
import dev.httpmarco.polocloud.shared.groups.Group;
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider;
import dev.httpmarco.polocloud.v1.groups.FindGroupRequest;
import dev.httpmarco.polocloud.v1.groups.GroupControllerGrpc;
import io.grpc.ManagedChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GroupProvider implements SharedGroupProvider<Group> {

    private final GroupControllerGrpc.GroupControllerBlockingStub blockingStub;
    private final GroupControllerGrpc.GroupControllerFutureStub futureStub;

    public GroupProvider(ManagedChannel channel) {
        this.blockingStub = GroupControllerGrpc.newBlockingStub(channel);
        this.futureStub = GroupControllerGrpc.newFutureStub(channel);
    }

    @Override
    public @NotNull List<Group> findAll() {
        return blockingStub.find(FindGroupRequest.getDefaultInstance()).getGroupsList().stream().map(Group.Companion::bindSnapshot).toList();
    }

    @Override
    @Nullable
    public Group find(@NotNull String name) {
        return blockingStub.find(FindGroupRequest.newBuilder().setName(name).build()).getGroupsList().stream().map(Group.Companion::bindSnapshot).findFirst().orElse(null);
    }

    @Override
    @NotNull
    public CompletableFuture<List<Group>> findAllAsync() {
        return FutureConverterKt.completableFromGuava(futureStub.find(FindGroupRequest.newBuilder().build()), findGroupResponse -> findGroupResponse.getGroupsList().stream().map(Group.Companion::bindSnapshot).toList());
    }

    @Override
    public CompletableFuture<Group> findAsync(@NotNull String name) {
        return FutureConverterKt.completableFromGuava(futureStub.find(FindGroupRequest.newBuilder().setName(name).build()),
                findGroupResponse -> findGroupResponse.getGroupsList().stream().map(Group.Companion::bindSnapshot).findFirst().orElse(null));
    }
}
