package dev.httpmarco.polocloud.suite.services.local;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.suite.PolocloudSuite;

public final class LocalClusterServiceQueue extends Thread {

    public LocalClusterServiceQueue() {
        setName("polocloud-local-service-queue");
    }

    @Override
    public void run() {
        while (interrupted()) {
            for (var group : PolocloudSuite.instance().groupProvider().findAll()) {

                if(group.minOnlineService() <= group.runningServicesAmount()) {
                    // all fine here, enough running services
                    continue;
                }

                // todo check percentage of service
                // todo start the new service

            }
        }
    }
}
