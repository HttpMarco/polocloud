package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class CloudGroupImpl implements CloudGroup {

    private String name;
    private String platform;
    private int memory;
    private int minOnlineServices;

    @Override
    public PropertiesPool properties() {
        return null;
    }

    @Override
    public int onlineAmount() {
        return CloudAPI.instance().serviceProvider().services(this).size();
    }

    @Override
    public String toString() {
        return "platform='" + platform + '\'' +
                ", memory=" + memory +
                ", minOnlineServices=" + minOnlineServices;
    }
}
