package dev.httpmarco.polocloud.suite.services.queue;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ServiceQueue extends Thread {

    public ServiceQueue() {
        setName("polocloud-service-queue");
    }

    @Override
    public void run() {

        while (!interrupted()) {

            Polocloud.instance().groupProvider().findAll().forEach(it -> {

                if(it.minOnlineService() == 0) {
                    return;
                }

                if(it.maxOnlineService() <= it.runningServicesAmount()) {
                    return;
                }

                // todo boot
                // todo check memory
                PolocloudSuite.instance().serviceProvider().bootNewInstance(it);
            });

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                log.error("Failed to sleep on queue thread.", e);
            }
        }
    }
}
