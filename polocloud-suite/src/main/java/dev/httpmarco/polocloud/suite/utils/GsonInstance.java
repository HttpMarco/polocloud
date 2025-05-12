package dev.httpmarco.polocloud.suite.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.cluster.ClusterConfig;
import dev.httpmarco.polocloud.suite.cluster.configuration.serializer.ClusterConfigSerializer;
import dev.httpmarco.polocloud.suite.cluster.configuration.serializer.ExternalSuiteSerializer;
import dev.httpmarco.polocloud.suite.cluster.global.suites.ExternalSuite;
import dev.httpmarco.polocloud.suite.i18n.serializer.LocalSerializer;
import dev.httpmarco.polocloud.suite.services.codec.ClusterServiceSerializer;

import java.util.Locale;

public final class GsonInstance {

    public static final Gson DEFAULT = new GsonBuilder().setPrettyPrinting()
            // if no cluster present -> no token present
            .registerTypeAdapter(ClusterConfig.class, new ClusterConfigSerializer())
            // for correct display of the language key
            .registerTypeAdapter(Locale.class, new LocalSerializer())
            // for a good redis external layout
            .registerTypeAdapter(ExternalSuite.class, new ExternalSuiteSerializer())
            .registerTypeHierarchyAdapter(ClusterService.class, new ClusterServiceSerializer())
            .create();

}
