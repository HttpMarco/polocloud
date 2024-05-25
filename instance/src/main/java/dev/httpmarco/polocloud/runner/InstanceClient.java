package dev.httpmarco.polocloud.runner;

import dev.httpmarco.osgan.networking.client.NettyClient;
import dev.httpmarco.osgan.networking.client.NettyClientBuilder;
import dev.httpmarco.polocloud.api.packets.CloudServiceRegisterPacket;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public class InstanceClient {

    private final NettyClient transmitter;

    public InstanceClient(String hostname, int port) {
        this.transmitter = new NettyClientBuilder()
                .withHostname("127.0.0.1")
                .onActive(channelTransmit -> {
                    channelTransmit.sendPacket(new CloudServiceRegisterPacket(UUID.fromString(System.getenv("serviceId"))));
                }).onInactive(channelTransmit -> {
                    System.out.println("b");
                }).withConnectTimeout(10000).build();
    }
}
