package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.base.CloudBase;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.Callable;

@Getter
@Accessors(fluent = true)
public final class CloudServiceGroupProvider implements CloudGroupProvider {

    private final List<CloudGroup> groups;
    private final CloudGroupServiceTypeAdapter groupServiceTypeAdapter = new CloudGroupServiceTypeAdapter();
    private final CloudGroupPlatformService platformService = new CloudGroupPlatformService();

    public CloudServiceGroupProvider() {
        this.groups = groupServiceTypeAdapter.readGroups();
        CloudBase.instance().logger().info("Loading following groups&2: &1" + String.join(", ", groups.stream().map(CloudGroup::name).toList()));
    }

    @Override
    public boolean createGroup(String name, String platform, int memory, int minOnlineCount) {

        if (isGroup(name)) {
            CloudAPI.instance().logger().info("The group already exists!");
            return false;
        }

        if (memory <= 0) {
            CloudAPI.instance().logger().info("The minimum memory value must be higher then 0. ");
            return false;
        }

        if (!platformService.isValidPlatform(platform)) {
            CloudAPI.instance().logger().info("The platform " + platform + " is an invalid type!");
            return false;
        }

        var group = new CloudGroupImpl(name, platform, memory, minOnlineCount);
        this.groupServiceTypeAdapter.includeFile(group);
        this.groups.add(group);
        return true;
    }

    @Override
    public boolean deleteGroup(String name) {

        if (!isGroup(name)) {
            CloudAPI.instance().logger().info("The group does not exists!");
            return false;
        }

        var group = group(name);
        CloudAPI.instance().serviceProvider().services(group).forEach(CloudService::shutdown);
        this.groupServiceTypeAdapter.excludeFile(group);
        this.groups.remove(group);
        return true;
    }

    @Override
    public boolean isGroup(String name) {
        return groups.stream().anyMatch(it -> it.name().equalsIgnoreCase(name));
    }

    @Override
    public CloudGroup group(String name) {
        return this.groups.stream().filter(it -> it.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public void update(CloudGroup cloudGroup) {
        this.groupServiceTypeAdapter.updateFile(cloudGroup);
    }
}
