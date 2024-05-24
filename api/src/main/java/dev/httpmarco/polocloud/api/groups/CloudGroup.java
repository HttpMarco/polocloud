package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;

public interface CloudGroup {

    String name();

    String platform();

    int memory();

    int minOnlineServices();

    PropertiesPool<GroupProperties<?>> properties();

    int onlineAmount();

    default void update() {
        CloudAPI.instance().groupProvider().update(this);
    }
}
