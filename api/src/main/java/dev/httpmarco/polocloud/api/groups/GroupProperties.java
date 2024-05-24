package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.properties.Property;

public class GroupProperties<T> extends Property<T> {

    public GroupProperties(String id) {
        super(id);
    }

    public static GroupProperties<String[]> TEMPLATES = new GroupProperties<>("templates");

}
