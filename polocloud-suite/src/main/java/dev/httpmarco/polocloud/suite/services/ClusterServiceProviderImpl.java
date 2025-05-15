package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.Closeable;
import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.api.services.events.ClusterServiceOnlineEvent;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.storage.ClusterStorage;
import dev.httpmarco.polocloud.suite.services.commands.ServiceCommand;
import dev.httpmarco.polocloud.suite.services.factory.LocalServiceFactory;
import dev.httpmarco.polocloud.suite.services.factory.ServiceFactory;
import dev.httpmarco.polocloud.suite.services.queue.ServiceLogQueue;
import dev.httpmarco.polocloud.suite.services.queue.ServiceQueue;
import dev.httpmarco.polocloud.suite.services.queue.ServiceTrackingQueue;
import dev.httpmarco.polocloud.suite.services.storage.LocalServiceStorage;
import dev.httpmarco.polocloud.suite.utils.PortDetector;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.UUID;

@Log4j2
@Accessors(fluent = true)
public final class ClusterServiceProviderImpl implements ClusterServiceProvider, Closeable {

    @Getter
    private final ClusterStorage<String, ClusterService> storage;

    private final ServiceTrackingQueue trackingQueue;
    private final ServiceLogQueue logQueue;
    private final ServiceQueue queue;

    @Getter
    private final ServiceFactory factory;

    public ClusterServiceProviderImpl() {
        //todo add external access
        this.storage = new LocalServiceStorage();
        this.storage.initialize();

        // todo add external access
        this.factory = new LocalServiceFactory();

        PolocloudSuite.instance().commandService().registerCommand(new ServiceCommand(this));

        // register default events
        PolocloudSuite.instance().eventProvider().subscribe(ClusterServiceOnlineEvent.class, it -> {

        });

        this.trackingQueue = new ServiceTrackingQueue(this);
        this.trackingQueue.start();

        this.logQueue = new ServiceLogQueue(this);
        this.logQueue.start();

        this.queue = new ServiceQueue();
        this.queue.start();
    }

    @Override
    public List<ClusterService> findAll() {
        return this.storage.items();
    }

    @Override
    public ClusterService find(String name) {
        return this.storage.singleton(name);
    }

    public ClusterService find(UUID uuid) {
        return this.storage.items().stream().filter(it -> it.uniqueId().equals(uuid)).findFirst().orElse(null);
    }

    public void bootNewInstance(ClusterGroup group) {
        var nextPossibleGroupServicePort = PortDetector.nextPort(group);
        //todo find id
        var service = new ClusterLocalServiceImpl(1, nextPossibleGroupServicePort, UUID.randomUUID(), group);

        log.info(PolocloudSuite.instance().translation().get("suite.cluster.service.starting", service.name()));

        this.storage.publish(service);
        this.factory.bootInstance(service);
    }

    @Override
    public void close() {
        this.queue.interrupt();

        log.info(PolocloudSuite.instance().translation().get("suite.cluster.service.queueStopped"));

        for (ClusterService item : this.storage.items()) {
            // only stop this suite instance
            if (item instanceof ClusterLocalServiceImpl localService) {
                localService.shutdown();
            }
        }
        this.trackingQueue.interrupt();
        this.logQueue.interrupt();
    }
}
