package dev.httpmarco.polocloud.sdk.java.information;

import dev.httpmarco.polocloud.shared.information.SharedCloudInformationProvider;
import dev.httpmarco.polocloud.shared.information.CloudInformation;
import dev.httpmarco.polocloud.v1.information.CloudInformationControllerGrpc;
import dev.httpmarco.polocloud.v1.information.GetCloudInformationRequest;
import io.grpc.ManagedChannel;
import org.jetbrains.annotations.NotNull;

public final class CloudInformationProvider implements SharedCloudInformationProvider<CloudInformation> {

    private final CloudInformationControllerGrpc.CloudInformationControllerBlockingStub blockingStub;

    public CloudInformationProvider(ManagedChannel channel) {
        this.blockingStub = CloudInformationControllerGrpc.newBlockingStub(channel);
    }

    @Override
    public @NotNull CloudInformation get() {
        return CloudInformation.Companion.bindSnapshot(blockingStub.get(GetCloudInformationRequest.getDefaultInstance()).getInformation());
    }
}
