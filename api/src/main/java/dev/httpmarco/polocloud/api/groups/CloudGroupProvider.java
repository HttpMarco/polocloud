package dev.httpmarco.polocloud.api.groups;

import java.util.List;

public interface CloudGroupProvider {

    void createGroup(String name, String platform, int memory, int minOnlineCount);

    boolean isGroup(String name);

    List<CloudGroup> groups();

}