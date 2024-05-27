package dev.httpmarco.polocloud.runner;

import dev.httpmarco.osgan.networking.client.NettyClient;
import dev.httpmarco.osgan.networking.client.NettyClientBuilder;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceRegisterPacket;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class InstanceClient {

    private final NettyClient transmitter;

    public InstanceClient(String hostname, int port) {
        this.transmitter = new NettyClientBuilder()
                .withHostname("127.0.0.1")
                .onActive(channelTransmit -> {
                    channelTransmit.sendPacket(new CloudServiceRegisterPacket(Instance.SERVICE_ID));
                }).onInactive(channelTransmit -> {
                }).withConnectTimeout(10000).build();
    }
}
