package dev.httpmarco.polocloud.api.groups;

public interface CloudGroupService {

    void createGroup(String name, int memory, int minOnlineCount);

}