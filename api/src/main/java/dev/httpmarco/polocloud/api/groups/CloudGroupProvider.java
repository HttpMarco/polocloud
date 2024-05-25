package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;

import java.util.List;

public interface CloudGroupProvider {

    boolean createGroup(String name, String platform, int memory, int minOnlineCount);

    boolean deleteGroup(String name);

    boolean isGroup(String name);

    CloudGroup group(String name);

    List<CloudGroup> groups();

    void update(CloudGroup cloudGroup);

    CloudGroup fromPacket(CodecBuffer buffer);

}