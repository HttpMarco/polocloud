package dev.httpmarco.polocloud.suite.services.queue;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.services.ClusterLocalService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ServiceQueue extends Thread {

    // we use a volatile boolean to ensure that the running state is visible across threads
    private volatile boolean running = true;

    public ServiceQueue() {
        setName("polocloud-service-queue");
    }

    @Override
    public void run() {
        while (running) {
            int maxQueueProcesses = PolocloudSuite.instance().config().local().maxQueueProcesses();
            if (maxQueueProcesses != -1 && Polocloud.instance().serviceProvider()
                    .findAll()
                    .stream()
                    .filter(s -> s instanceof ClusterLocalService)
                    .filter(s -> s.state() == ClusterServiceState.STARTING)
                    .count() < maxQueueProcesses) {

                Polocloud.instance().groupProvider().findAll().forEach(it -> {
                    if (it.minOnlineService() == 0) {
                        return;
                    }

                    if (it.maxOnlineService() <= it.runningServicesAmount()) {
                        return;
                    }

                    // todo boot
                    // todo check memory
                    PolocloudSuite.instance().serviceProvider().bootNewInstance(it);
                });
            }


            try {
                sleep(1000);
            } catch (InterruptedException ignore) {
            }
        }
    }

    @Override
    public void interrupt() {
        this.running = false;
        super.interrupt();
    }
}
