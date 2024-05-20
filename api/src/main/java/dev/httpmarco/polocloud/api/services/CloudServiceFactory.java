package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.polocloud.api.groups.CloudGroup;

public interface CloudServiceFactory {

    void start(CloudGroup cloudGroup);

    void stop(CloudService service);

}
