package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.storage.ClusterStorage;
import dev.httpmarco.polocloud.suite.services.commands.ServiceCommand;
import dev.httpmarco.polocloud.suite.services.storage.LocalServiceStorage;

import java.util.List;

public class ClusterServiceProviderImpl implements ClusterServiceProvider {

    private final ClusterStorage<String, ClusterService> storage;

    public ClusterServiceProviderImpl() {
        //todo add external access
        this.storage = new LocalServiceStorage();
        this.storage.initialize();

        PolocloudSuite.instance().commandService().registerCommand(new ServiceCommand(this));
    }

    @Override
    public List<ClusterService> findAll() {
        return this.storage.items();
    }
}
