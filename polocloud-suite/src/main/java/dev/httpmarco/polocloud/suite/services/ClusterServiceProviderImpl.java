package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.storage.ClusterStorage;
import dev.httpmarco.polocloud.suite.services.commands.ServiceCommand;
import dev.httpmarco.polocloud.suite.services.factory.LocalServiceFactory;
import dev.httpmarco.polocloud.suite.services.factory.ServiceFactory;
import dev.httpmarco.polocloud.suite.services.queue.ServiceQueue;
import dev.httpmarco.polocloud.suite.services.storage.LocalServiceStorage;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.UUID;

@Log4j2
public class ClusterServiceProviderImpl implements ClusterServiceProvider {

    private final ClusterStorage<String, ClusterService> storage;
    private final ServiceFactory factory;
    private final ServiceQueue queue;

    public ClusterServiceProviderImpl() {
        //todo add external access
        this.storage = new LocalServiceStorage();
        this.storage.initialize();

        // todo add external access
        this.factory = new LocalServiceFactory();

        PolocloudSuite.instance().commandService().registerCommand(new ServiceCommand(this));

        this.queue = new ServiceQueue();
        this.queue.start();
    }

    @Override
    public List<ClusterService> findAll() {
        return this.storage.items();
    }

    public void bootNewInstance(ClusterGroup group) {
        var service = new ClusterServiceImpl(1, UUID.randomUUID(), group);

        log.info("Service &8'&f{}&8' &7is starting now&8...", service.name());
        this.storage.publish(service);

        this.factory.bootInstance(service);
    }
}
