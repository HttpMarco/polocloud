package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;

import java.util.List;
import java.util.UUID;

public interface CloudService {

    CloudGroup group();

    default String name() {
        return group().name() + "-" + orderedId();
    }

    int orderedId();

    UUID id();

    int port();

    List<String> log();

    default void shutdown() {
        CloudAPI.instance().serviceProvider().factory().stop(this);
    }

}
