package dev.httpmarco.polocloud.api.properties;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public final class PropertiesPool {

    private final Map<Property<?>, Object> properties = new HashMap<>();



}
