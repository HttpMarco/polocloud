package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;

public interface ClusterService extends Named {

    ClusterGroup group();

}
