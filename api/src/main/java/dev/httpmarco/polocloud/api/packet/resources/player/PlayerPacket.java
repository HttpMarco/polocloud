package dev.httpmarco.polocloud.api.packet.resources.player;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.players.ClusterPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class PlayerPacket extends Packet {

    private ClusterPlayer player;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        var nullResult = packetBuffer.readBoolean();

        if (!nullResult) {
            this.player = CloudAPI.instance().playerProvider().read(packetBuffer);
        }
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(player == null);

        if (player != null) {
            CloudAPI.instance().playerProvider().write(player, packetBuffer);
        }
    }
}
