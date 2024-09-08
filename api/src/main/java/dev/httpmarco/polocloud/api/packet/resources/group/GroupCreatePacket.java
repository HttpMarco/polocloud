package dev.httpmarco.polocloud.api.packet.resources.group;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class GroupCreatePacket extends Packet {

    private String name;
    private String[] templates;
    private String[] nodes;
    private PlatformGroupDisplay platformGroupDisplay;
    private int maxMemory;
    private boolean staticService;
    private int minOnline;
    private int maxOnline;
    private boolean fallbackGroup;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.name = packetBuffer.readString();

        var nodeSize = packetBuffer.readInt();
        var nodes = new HashSet<String>();

        for (int i = 0; i < nodeSize; i++) {
            nodes.add(packetBuffer.readString());
        }
        this.nodes = nodes.toArray(String[]::new);

        var templateSize = packetBuffer.readInt();
        var templates = new HashSet<String>();

        for (int i = 0; i < templateSize; i++) {
            templates.add(packetBuffer.readString());
        }
        this.templates = templates.toArray(String[]::new);

        var platform = packetBuffer.readString();
        var version = packetBuffer.readString();
        var type = packetBuffer.readEnum(PlatformType.class);
        this.platformGroupDisplay = new PlatformGroupDisplay(platform, version, type);

        this.maxMemory = packetBuffer.readInt();
        this.staticService = packetBuffer.readBoolean();
        this.minOnline = packetBuffer.readInt();
        this.maxOnline = packetBuffer.readInt();
        this.fallbackGroup = packetBuffer.readBoolean();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeString(name);
        packetBuffer.writeInt(nodes.length);

        for (String node : nodes) {
            packetBuffer.writeString(node);
        }

        packetBuffer.writeInt(templates.length);

        for (String template : templates) {
            packetBuffer.writeString(template);
        }

        packetBuffer.writeString(platformGroupDisplay.platform());
        packetBuffer.writeString(platformGroupDisplay.version());

        packetBuffer.writeInt(maxMemory);
        packetBuffer.writeBoolean(staticService);
        packetBuffer.writeInt(minOnline);
        packetBuffer.writeInt(maxOnline);
        packetBuffer.writeBoolean(fallbackGroup);
    }
}
