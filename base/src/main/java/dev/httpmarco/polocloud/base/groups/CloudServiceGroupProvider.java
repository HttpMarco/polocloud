package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.base.CloudBase;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudServiceGroupProvider implements CloudGroupProvider {

    private final List<CloudGroup> groups;
    private final CloudGroupPlatformService platformService = new CloudGroupPlatformService();
    private final CloudGroupServiceTypeAdapter groupServiceTypeAdapter = new CloudGroupServiceTypeAdapter(platformService);

    public CloudServiceGroupProvider() {
        this.groups = groupServiceTypeAdapter.readGroups();
        CloudBase.instance().logger().info("Loading following groups&2: &3" + String.join("&2, &3", groups.stream().map(CloudGroup::name).toList()));
    }

    @Override
    public boolean createGroup(String name, String platformVersion, int memory, int minOnlineCount) {
        if (isGroup(name)) {
            CloudAPI.instance().logger().info("The group already exists!");
            return false;
        }

        if (memory <= 0) {
            CloudAPI.instance().logger().info("The minimum memory value must be higher then 0. ");
            return false;
        }

        if (!platformService.isValidPlatform(platformVersion)) {
            CloudAPI.instance().logger().info("The platform " + platformVersion + " is an invalid type!");
            return false;
        }

        var platform = platformService.find(platformVersion);

        var group = new CloudGroupImpl(name, new PlatformVersion(platformVersion, platform.proxy()), memory, minOnlineCount);
        this.groupServiceTypeAdapter.includeFile(group);
        this.groups.add(group);


        return true;
    }

    @Override
    public boolean deleteGroup(String name) {

        if (!isGroup(name)) {
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

    @Override
    public CloudGroup fromPacket(CodecBuffer buffer) {
        //todo
        return null;
    }
}
