package dev.httpmarco.polocloud.sdk.java.information;

import dev.httpmarco.polocloud.shared.information.SharedCloudInformationProvider;
import dev.httpmarco.polocloud.shared.information.CloudInformation;
import dev.httpmarco.polocloud.v1.information.CloudInformationControllerGrpc;
import dev.httpmarco.polocloud.v1.information.CloudInformationFindAllRequest;
import dev.httpmarco.polocloud.v1.information.CloudInformationFindRequest;
import io.grpc.ManagedChannel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CloudInformationProvider implements SharedCloudInformationProvider<CloudInformation> {

    private final CloudInformationControllerGrpc.CloudInformationControllerBlockingStub blockingStub;

    public CloudInformationProvider(ManagedChannel channel) {
        this.blockingStub = CloudInformationControllerGrpc.newBlockingStub(channel);
    }

    @Override
    public @NotNull CloudInformation find() {
        return CloudInformation.Companion.bindSnapshot(blockingStub.find(CloudInformationFindRequest.getDefaultInstance()).getInformation(0));
    }

    @Override
    public @NotNull List<CloudInformation> find(long from, long to) {
        return blockingStub.find(CloudInformationFindRequest.newBuilder().setFrom(from).setTo(to).build()).getInformationList().stream().map(CloudInformation.Companion::bindSnapshot).toList();
    }

    @Override
    @NotNull
    public List<CloudInformation> findAll() {
        return blockingStub.findAll(CloudInformationFindAllRequest.getDefaultInstance()).getInformationList().stream().map(CloudInformation.Companion::bindSnapshot).toList();
    }
}
