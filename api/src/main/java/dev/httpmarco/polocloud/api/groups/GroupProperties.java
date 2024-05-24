package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.properties.Property;

public class GroupProperties<T> extends Property<T> {

    public GroupProperties(String id, Class<T> type) {
        super(id, type);
    }

    public static GroupProperties<String> TEMPLATES = new GroupProperties<>("templates", String.class);

}
