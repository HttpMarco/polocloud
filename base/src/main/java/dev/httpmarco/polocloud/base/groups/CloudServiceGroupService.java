package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.CloudGroupService;
import dev.httpmarco.polocloud.base.CloudBase;

public final class CloudServiceGroupService implements CloudGroupService {

    private final CloudGroupServiceTypeAdapter groupServiceTypeAdapter = new CloudGroupServiceTypeAdapter();

    public CloudServiceGroupService() {
        CloudBase.instance().logger().info("Loading following groups: " + String.join(", ", groupServiceTypeAdapter.readGroups().stream().map(CloudGroup::name).toList()));
    }

    @Override
    public void createGroup(String name, int memory, int minOnlineCount) {
        this.groupServiceTypeAdapter.includeFile(new CloudGroupImpl(name, memory, minOnlineCount));
    }
}
