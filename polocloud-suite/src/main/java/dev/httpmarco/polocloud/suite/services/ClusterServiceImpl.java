package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public final class ClusterServiceImpl implements ClusterService {

    private String name;

}
