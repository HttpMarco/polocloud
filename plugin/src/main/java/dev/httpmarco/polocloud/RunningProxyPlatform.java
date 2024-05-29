package dev.httpmarco.polocloud;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.events.service.CloudServiceOnlineEvent;
import dev.httpmarco.polocloud.api.events.service.CloudServiceShutdownEvent;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import dev.httpmarco.polocloud.api.services.ServiceState;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public class RunningProxyPlatform extends RunningPlatform {

    public RunningProxyPlatform(Consumer<CloudService> register, Consumer<CloudService> unRegister) {
        var instance = CloudAPI.instance();

        for (var service : CloudAPI.instance().serviceProvider().filterService(ServiceFilter.SERVERS)) {
            register.accept(service);
        }

        instance.globalEventNode().addListener(CloudServiceOnlineEvent.class, event -> {
            if (event.cloudService().group().platform().proxy() || event.cloudService().state() != ServiceState.ONLINE) {
                return;
            }
            register.accept(event.cloudService());
        });

        instance.globalEventNode().addListener(CloudServiceShutdownEvent.class, event -> unRegister.accept(event.cloudService()));
    }
}
