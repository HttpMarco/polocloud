package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.osgan.utils.executers.FutureResult;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface CloudServiceProvider {

    CloudServiceFactory factory();

    List<CloudService> services();

    CompletableFuture<List<CloudService>> servicesAsync();

    List<CloudService> services(CloudGroup group);

    CloudService find(UUID id);

    CloudService service(String name);

    CloudService fromPacket(CloudGroup parent, CodecBuffer buffer);



}
