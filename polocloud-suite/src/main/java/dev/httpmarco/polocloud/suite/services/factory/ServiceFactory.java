package dev.httpmarco.polocloud.suite.services.factory;

import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;

public interface ServiceFactory {

    void bootInstance(ClusterLocalServiceImpl service);

}
