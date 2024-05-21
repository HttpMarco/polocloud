package dev.httpmarco.polocloud.runner;

import dev.httpmarco.osgan.networking.client.NettyClient;
import dev.httpmarco.osgan.networking.client.NettyClientBuilder;

public class InstanceClient {

    private final NettyClient client;

    public InstanceClient(String hostname, int port) {
        System.out.println("trying to connect to " + hostname + ":" + port);
        this.client = new NettyClientBuilder()
                .withHostname("127.0.0.1")
                .onActive(channelTransmit -> {
                    System.out.println("a");
                }).onInactive(channelTransmit -> {
                    System.out.println("b");
                }).withConnectTimeout(10000).build();
    }
}
