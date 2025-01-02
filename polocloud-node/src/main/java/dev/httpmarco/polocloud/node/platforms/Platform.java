package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroupType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class Platform implements Named {

    private final String name;
    private final String icon;
    private final ClusterGroupType type;
    private final String url;
    private final List<PlatformVersion> versions = new ArrayList<>();

}
