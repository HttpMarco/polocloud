package dev.httpmarco.polocloud.sdk.java.platform;

import dev.httpmarco.polocloud.shared.platform.Platform;
import dev.httpmarco.polocloud.shared.platform.SharedPlatformProvider;
import dev.httpmarco.polocloud.v1.GroupType;
import dev.httpmarco.polocloud.v1.platform.PlatformControllerGrpc;
import dev.httpmarco.polocloud.v1.platform.PlatformFindRequest;
import io.grpc.ManagedChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlatformProvider implements SharedPlatformProvider<Platform> {

    private PlatformControllerGrpc.PlatformControllerBlockingStub blockingStub;

    public PlatformProvider(ManagedChannel channel) {
        this.blockingStub = PlatformControllerGrpc.newBlockingStub(channel);
    }

    @Override
    @NotNull
    public List<Platform> findAll() {
        return blockingStub.find(PlatformFindRequest.getDefaultInstance()).getPlatformsList().stream().map(Platform.Companion::bindSnapshot).toList();
    }

    @Override
    @Nullable
    public Platform find(@NotNull String name) {
        return blockingStub.find(PlatformFindRequest.newBuilder().setName(name).build()).getPlatformsList().stream().map(Platform.Companion::bindSnapshot).findFirst().orElse(null);
    }

    @Override
    @NotNull
    public List<Platform> find(@NotNull GroupType type) {
        return blockingStub.find(PlatformFindRequest.newBuilder().setType(type).build()).getPlatformsList().stream().map(Platform.Companion::bindSnapshot).toList();
    }
}
