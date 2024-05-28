package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.properties.Property;

import java.io.Serializable;

public class GroupProperties<T> extends Property<T> implements Serializable {

    public GroupProperties(String id, Class<T> type) {
        super(id, type);
    }

    public static GroupProperties<String> TEMPLATES = new GroupProperties<>("templates", String.class);

    public static GroupProperties<Integer> PORT_RANGE = new GroupProperties<>("portRange", int.class);

    public static GroupProperties<Boolean> FALLBACK = new GroupProperties<>("fallback", boolean.class);

}