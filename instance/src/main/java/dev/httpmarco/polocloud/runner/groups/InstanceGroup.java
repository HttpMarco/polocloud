package dev.httpmarco.polocloud.runner.groups;

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

//todo fix duplicated code
@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class InstanceGroup implements CloudGroup {

    private final String name;
    private final String platform;
    private final int memory;
    private final int minOnlineServices;


    @Override
    public PropertiesPool<GroupProperties<?>> properties() {
        //todo
        return null;
    }

    @Override
    public int onlineAmount() {
        //todo
        return 0;
    }

    @Override
    public void update() {
        //todo
    }
}
