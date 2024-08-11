package dev.httpmarco.polocloud.node.platforms.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class AbstractPlatformAction implements PlatformAction{

    private final String propertyId;

}
