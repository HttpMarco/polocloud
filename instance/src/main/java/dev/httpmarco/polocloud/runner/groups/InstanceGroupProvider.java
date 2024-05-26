package dev.httpmarco.polocloud.runner.groups;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.packets.CloudServiceRegisterPacket;
import dev.httpmarco.polocloud.runner.Instance;

import java.util.List;

public class InstanceGroupProvider implements CloudGroupProvider {

    @Override
    public boolean createGroup(String name, String platform, int memory, int minOnlineCount) {
        return false;
    }

    @Override
    public boolean deleteGroup(String name) {
        return false;
    }

    @Override
    public boolean isGroup(String name) {
        return false;
    }

    @Override
    public CloudGroup group(String name) {
        return null;
    }

    @Override
    public List<CloudGroup> groups() {
        System.out.println("sending packet");
        Instance.instance().client().transmitter().request("groups-all", CloudServiceRegisterPacket.class, it -> {

        });
        return List.of();
    }

    @Override
    public void update(CloudGroup cloudGroup) {

    }

    @Override
    public CloudGroup fromPacket(CodecBuffer buffer) {
        var name = buffer.readString();
        var platform = buffer.readString();
        var minOnlineServices = buffer.readInt();
        var memory = buffer.readInt();

        return new InstanceGroup(name, platform, minOnlineServices, memory);
    }
}
