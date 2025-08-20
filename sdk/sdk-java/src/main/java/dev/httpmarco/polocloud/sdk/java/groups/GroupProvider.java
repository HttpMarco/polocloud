package dev.httpmarco.polocloud.sdk.java.groups;

import dev.httpmarco.polocloud.common.future.FutureConverterKt;
import dev.httpmarco.polocloud.shared.groups.Group;
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider;
import dev.httpmarco.polocloud.v1.groups.*;
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

    @Override
    @Nullable
    public Group create(@NotNull Group group) {
        var snapshot = group.toSnapshot();
        return Group.Companion.bindSnapshot(blockingStub.create(
                GroupCreateRequest.newBuilder()
                        .setName(snapshot.getName())
                        .setType(snapshot.getType())
                        .setPlatform(snapshot.getPlatform())
                        .setMinimumMemory(snapshot.getMinimumMemory())
                        .setMaximumMemory(snapshot.getMaximumMemory())
                        .setMinimumOnline(snapshot.getMinimumOnline())
                        .setMaximumOnline(snapshot.getMaximumOnline())
                        .setPercentageToStartNewService(snapshot.getPercentageToStartNewService())
                        .addAllTemplates(snapshot.getTemplatesList())
                        .putAllProperties(snapshot.getPropertiesMap())
                        .build()
        ).getGroup());
    }

    @Override
    @NotNull
    public CompletableFuture<Group> createAsync(@NotNull Group group) {
        var snapshot = group.toSnapshot();
        return FutureConverterKt.completableFromGuava(futureStub.create(
                GroupCreateRequest.newBuilder()
                        .setName(snapshot.getName())
                        .setType(snapshot.getType())
                        .setPlatform(snapshot.getPlatform())
                        .setMinimumMemory(snapshot.getMinimumMemory())
                        .setMaximumMemory(snapshot.getMaximumMemory())
                        .setMinimumOnline(snapshot.getMinimumOnline())
                        .setMaximumOnline(snapshot.getMaximumOnline())
                        .setPercentageToStartNewService(snapshot.getPercentageToStartNewService())
                        .addAllTemplates(snapshot.getTemplatesList())
                        .putAllProperties(snapshot.getPropertiesMap())
                        .build()
        ), groupSnapshot -> Group.Companion.bindSnapshot(groupSnapshot.getGroup()));
    }

    @Override
    public Group update(@NotNull Group group) {
        var snapshot = group.toSnapshot();
        return Group.Companion.bindSnapshot(blockingStub.update(
                GroupUpdateRequest.newBuilder()
                        .setName(snapshot.getName())
                        .setType(snapshot.getType())
                        .setPlatform(snapshot.getPlatform())
                        .setMinimumMemory(snapshot.getMinimumMemory())
                        .setMaximumMemory(snapshot.getMaximumMemory())
                        .setMinimumOnline(snapshot.getMinimumOnline())
                        .setMaximumOnline(snapshot.getMaximumOnline())
                        .setPercentageToStartNewService(snapshot.getPercentageToStartNewService())
                        .addAllTemplates(snapshot.getTemplatesList())
                        .putAllProperties(snapshot.getPropertiesMap())
                        .build()
        ).getGroup());
    }

    @Override
    public CompletableFuture<Group> updateAsync(@NotNull Group group) {
        var snapshot = group.toSnapshot();
        return FutureConverterKt.completableFromGuava(futureStub.update(
                GroupUpdateRequest.newBuilder()
                        .setName(snapshot.getName())
                        .setType(snapshot.getType())
                        .setPlatform(snapshot.getPlatform())
                        .setMinimumMemory(snapshot.getMinimumMemory())
                        .setMaximumMemory(snapshot.getMaximumMemory())
                        .setMinimumOnline(snapshot.getMinimumOnline())
                        .setMaximumOnline(snapshot.getMaximumOnline())
                        .setPercentageToStartNewService(snapshot.getPercentageToStartNewService())
                        .addAllTemplates(snapshot.getTemplatesList())
                        .putAllProperties(snapshot.getPropertiesMap())
                        .build()
        ), groupSnapshot -> Group.Companion.bindSnapshot(groupSnapshot.getGroup()));
    }

    @Override
    @Nullable
    public Group delete(@NotNull String name) {
        return Group.Companion.bindSnapshot(blockingStub.delete(GroupDeleteRequest.newBuilder().setName(name).build()).getGroup());
    }
}
