package dev.httpmarco.polocloud.instance.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.instance.group.codec.ClusterGroupSerializer;
import dev.httpmarco.polocloud.instance.service.codec.ClusterServiceSerializer;

public final class GsonInstance {

    public static final Gson DEFAULT = new GsonBuilder().setPrettyPrinting()
            .registerTypeHierarchyAdapter(ClusterGroup.class, new ClusterGroupSerializer())
            .registerTypeHierarchyAdapter(ClusterService.class, new ClusterServiceSerializer())
            .create();

}
