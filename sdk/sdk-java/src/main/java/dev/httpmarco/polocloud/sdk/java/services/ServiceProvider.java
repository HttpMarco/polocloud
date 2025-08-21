package dev.httpmarco.polocloud.sdk.java.services;

import dev.httpmarco.polocloud.common.future.FutureConverterKt;
import dev.httpmarco.polocloud.sdk.java.Polocloud;
import dev.httpmarco.polocloud.shared.groups.Group;
import dev.httpmarco.polocloud.shared.service.Service;
import dev.httpmarco.polocloud.shared.service.SharedBootConfiguration;
import dev.httpmarco.polocloud.shared.service.SharedServiceProvider;
import dev.httpmarco.polocloud.v1.GroupType;
import dev.httpmarco.polocloud.v1.services.*;
import io.grpc.ManagedChannel;
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
        return blockingStub.find(ServiceFindRequest.newBuilder().setGroupName(group).build()).getServicesList().stream().map(Service.Companion::bindSnapshot).toList();
    }

    @Override
    public @NotNull CompletableFuture<List<Service>> findAllAsync() {
        return FutureConverterKt.completableFromGuava(futureStub.find(ServiceFindRequest.getDefaultInstance()), it -> it.getServicesList().stream().map(Service.Companion::bindSnapshot).toList());
    }

    @Override
    @NotNull
    public CompletableFuture<Service> findAsync(@NotNull String name) {
        return FutureConverterKt.completableFromGuava(futureStub.find(ServiceFindRequest.newBuilder().setName(name).build()), it -> it.getServicesList().stream().map(Service.Companion::bindSnapshot).findFirst().orElse(null));
    }

    @Override
    @NotNull
    public CompletableFuture<List<Service>> findByGroupAsync(@NotNull Group group) {
        return findByGroupAsync(group.getName());
    }

    @Override
    @NotNull
    public CompletableFuture<List<Service>> findByGroupAsync(@NotNull String group) {
        return FutureConverterKt.completableFromGuava(futureStub.find(ServiceFindRequest.newBuilder().setGroupName(group).build()), it -> it.getServicesList().stream().map(Service.Companion::bindSnapshot).toList());
    }

    @NotNull
    @Override
    public ServiceSnapshot bootInstanceWithConfiguration(@NotNull String name, @NotNull SharedBootConfiguration configuration) {
        Group group = Polocloud.instance().groupProvider().find(name);
        if(group == null) {
            throw new IllegalStateException("Group not found");
        }
        int minMemory = configuration.minMemory() != null ? configuration.minMemory() : group.getMinMemory();
        int maxMemory = configuration.maxMemory() != null ? configuration.maxMemory() : group.getMaxMemory();
        if (minMemory <= 0 || maxMemory <= 0) {
            throw new IllegalArgumentException("Minimum and maximum memory must be greater than 0.");
        }
        return this.blockingStub.bootWithConfiguration(
                ServiceBootWithConfigurationRequest.newBuilder()
                        .setGroupName(name)
                        .setMinimumMemory(minMemory)
                        .setMaximumMemory(maxMemory)
                        .addAllTemplates(configuration.templates())
                        .addAllExcludedTemplates(configuration.excludedTemplates())
                        .putAllProperties(configuration.properties())
                        .build()
        ).getService();
    }

    @NotNull
    @Override
    public ServiceSnapshot bootInstance(@NotNull String name) {
        return this.blockingStub.boot(ServiceBootRequest.newBuilder().setGroupName(name).build()).getService();
    }

    @Override
    @NotNull
    public List<Service> findByType(@NotNull GroupType type) {
        return blockingStub.find(ServiceFindRequest.newBuilder().setType(type).build()).getServicesList().stream().map(Service.Companion::bindSnapshot).toList();
    }

    @Override
    @NotNull
    public CompletableFuture<List<Service>> findByTypeAsync(@NotNull GroupType type) {
        return FutureConverterKt.completableFromGuava(futureStub.find(ServiceFindRequest.newBuilder().setType(type).build()), it -> it.getServicesList().stream().map(Service.Companion::bindSnapshot).toList());
    }

    @NotNull
    @Override
    public ServiceSnapshot shutdownService(@NotNull String name) {
        return blockingStub.shutdown(ServiceShutdownRequest.newBuilder().setName(name).build()).getService();
    }
}
