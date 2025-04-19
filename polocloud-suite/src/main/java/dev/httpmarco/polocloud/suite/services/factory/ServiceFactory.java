package dev.httpmarco.polocloud.suite.services.factory;

import dev.httpmarco.polocloud.api.services.ClusterService;

public interface ServiceFactory {

    void bootInstance(ClusterService service);

}
