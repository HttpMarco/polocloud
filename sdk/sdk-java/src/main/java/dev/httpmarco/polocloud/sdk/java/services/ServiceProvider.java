package dev.httpmarco.polocloud.sdk.java.services;

import dev.httpmarco.polocloud.common.future.FutureConverterKt;
import dev.httpmarco.polocloud.shared.groups.Group;
import dev.httpmarco.polocloud.shared.service.Service;
import dev.httpmarco.polocloud.shared.service.SharedBootConfiguration;
import dev.httpmarco.polocloud.shared.service.SharedServiceProvider;
import dev.httpmarco.polocloud.v1.GroupType;
import dev.httpmarco.polocloud.v1.services.ServiceControllerGrpc;
import dev.httpmarco.polocloud.v1.services.ServiceFindRequest;
import io.grpc.ManagedChannel;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ServiceProvider implements SharedServiceProvider<Service> {

    private final ServiceControllerGrpc.ServiceControllerFutureStub futureStub;
    private final ServiceControllerGrpc.ServiceControllerBlockingStub blockingStub;

    public ServiceProvider(ManagedChannel channel) {
        this.blockingStub = ServiceControllerGrpc.newBlockingStub(channel);
        this.futureStub = ServiceControllerGrpc.newFutureStub(channel);
    }

    @Override
    public @NotNull List<Service> findAll() {
        return blockingStub.find(ServiceFindRequest.getDefaultInstance()).getServicesList().stream().map(Service.Companion::bindSnapshot).toList();
    }

    @Override
    @Nullable
    public Service find(@NotNull String name) {
        return blockingStub.find(ServiceFindRequest.newBuilder().setName(name).build()).getServicesList().stream().map(Service.Companion::bindSnapshot).findFirst().orElse(null);
    }

    @Override
    @NotNull
    public List<Service> findByGroup(@NotNull Group group) {
        return this.findByGroup(group.getName());
    }

    @Override
    @NotNull
    public List<Service> findByGroup(@NotNull String group) {
        return List.of();
    }

    @Override
    public @NotNull CompletableFuture<List<Service>> findAllAsync() {
        return FutureConverterKt.completableFromGuava(futureStub.find(ServiceFindRequest.getDefaultInstance()), it -> it.getServicesList().stream().map(Service.Companion::bindSnapshot).toList());
    }

    @Override
    @NotNull
    public CompletableFuture<Service> findAsync(@NotNull String name) {
        return null;
    }

    @Override
    @NotNull
    public CompletableFuture<List<Service>> findByGroupAsync(@NotNull Group group) {
        return null;
    }

    @Override
    @NotNull
    public CompletableFuture<List<Service>> findByGroupAsync(@NotNull String group) {
        return null;
    }

    @Override
    public void bootInstanceWithConfiguration(@NotNull String name, @NotNull Function1<? super SharedBootConfiguration, ?> configuration) {

    }

    @Override
    public void bootInstance(@NotNull String name) {

    }

    @Override
    @NotNull
    public List<Service> findByType(@NotNull GroupType type) {
        return List.of();
    }

    @Override
    @NotNull
    public CompletableFuture<List<Service>> findByTypeAsync(@NotNull GroupType type) {
        return null;
    }

    @Override
    public void shutdownService(@NotNull String name) {

    }
}
