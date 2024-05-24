package dev.httpmarco.polocloud.api.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class Property<T> {

    private String id;

}
