package dev.httpmarco.polocloud.api.groups;

import java.util.Collection;

public interface ClusterGroupProvider {

    Collection<ClusterGroup> findAll();

}
