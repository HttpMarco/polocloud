package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.polocloud.api.groups.CloudGroup;

import java.util.List;

public interface CloudServiceProvider {

    CloudServiceFactory factory();

    List<CloudService> services();

    List<CloudService> services(CloudGroup group);

}
