package dev.httpmarco.polocloud.api.packet.resources.player;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.players.ClusterPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public final class PlayerCollectionPacket extends Packet {

    private List<ClusterPlayer> players;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        players = new ArrayList<>();
        var amount = packetBuffer.readInt();

        for (int i = 0; i < amount; i++) {
            players.add(CloudAPI.instance().playerProvider().read(packetBuffer));
        }
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeInt(players.size());
        for (var player : players) {
            CloudAPI.instance().playerProvider().write(player, packetBuffer);
        }
    }
}
