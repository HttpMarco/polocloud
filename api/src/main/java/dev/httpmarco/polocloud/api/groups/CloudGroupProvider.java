package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.osgan.files.json.JsonUtils;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;

import java.util.List;

public interface CloudGroupProvider {

    boolean createGroup(String name, String platform, int memory, int minOnlineCount);

    boolean deleteGroup(String name);

    boolean isGroup(String name);

    CloudGroup group(String name);

    List<CloudGroup> groups();

    void update(CloudGroup cloudGroup);

    default CloudGroup fromPacket(CodecBuffer buffer) {
        CloudGroup group = JsonUtils.fromJson(buffer.readString(), CloudGroup.class);
        if (this.isGroup(group.name())) return group;
        return null;
    }

}