package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class CloudServiceQueue extends Thread {

    private CloudServiceProvider serviceProvider;

    @Override
    public void run() {
        while (!isInterrupted()) {
            for (var group : CloudAPI.instance().groupProvider().groups()) {
                var onlineDiff = group.onlineAmount() - group.minOnlineServices();

                if (onlineDiff < 0) {
                    for (int i = 0; i < (-onlineDiff); i++) {
                        serviceProvider.factory().start(group);
                    }
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {
            }
        }
    }
}
