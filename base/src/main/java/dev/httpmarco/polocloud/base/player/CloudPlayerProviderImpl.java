/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.base.player;

import dev.httpmarco.osgan.networking.CommunicationFuture;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.packets.player.*;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.api.player.CloudPlayerProvider;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.SneakyThrows;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public final class CloudPlayerProviderImpl implements CloudPlayerProvider {

    private final List<CloudPlayer> players = new CopyOnWriteArrayList<>();

    public CloudPlayerProviderImpl() {
        var transmitter = CloudBase.instance().transmitter();

        transmitter.responder("players-all", property -> new CloudAllPlayersPacket(this.players));
        transmitter.responder("player-get", properties -> new CloudPlayerPacket(this.find(properties.getUUID("uniqueId"))));

        transmitter.listen(CloudPlayerRegisterPacket.class, (channelTransmit, packet) -> {
            this.register(packet.id(), packet.name());
            this.find(packet.id()).currentProxyName(CloudAPI.instance().serviceProvider().find(packet.proxyId()).name());
        });

        transmitter.listen(CloudPlayerUnregisterPacket.class, (channelTransmit, packet) -> this.unregister(packet.id()));

        transmitter.listen(CloudPlayerConnectToServerPacket.class, (channelTransmit, packet) -> {
            var name = find(packet.uuid()).currentProxyName();
            var service = CloudAPI.instance().serviceProvider().find(name);

            if (service != null) {
                if (service instanceof LocalCloudService localCloudService) {
                    localCloudService.channelTransmit().sendPacket(packet);
                } else {
                    //todo
                }
            }
        });

        transmitter.listen(CloudPlayerServiceChangePacket.class, (channelTransmit, packet) -> {
            var cloudPlayer = this.find(packet.id());
            cloudPlayer.currentServerName(CloudAPI.instance().serviceProvider().find(packet.serviceId()).name());
        });
    }

    @Override
    public List<CloudPlayer> players() {
        return this.players;
    }

    @Override
    public CompletableFuture<List<CloudPlayer>> playersAsync() {
        return CommunicationFuture.completedFuture(this.players);
    }

    @Override
    public void register(UUID id, String name) {
        this.players.add(new CloudPlayerImpl(id, name));
    }

    @Override
    public void unregister(UUID id) {
        this.players.removeIf(it -> it.uniqueId().equals(id));
    }

    @Override
    @SneakyThrows
    public CloudPlayer find(UUID id) {
        return this.findAsync(id).get(5, TimeUnit.SECONDS);
    }

    @Override
    public CompletableFuture<CloudPlayer> findAsync(UUID id) {
        var future = new CommunicationFuture<CloudPlayer>();
        future.complete(this.players.stream().filter(it -> it.uniqueId().equals(id)).findFirst().orElse(null));
        return future.toCompletableFuture();
    }

    @Override
    public CloudPlayer fromPacket(PacketBuffer buffer) {
        var uuid = buffer.readUniqueId();
        var player = this.find(uuid);
        if (player != null) return player;
        this.register(uuid, buffer.readString());
        return this.find(uuid);
    }
}
