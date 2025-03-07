package dev.httpmarco.polocloud.suite.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.httpmarco.polocloud.suite.cluster.ClusterConfig;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterConfigSerializer;
import dev.httpmarco.polocloud.suite.cluster.global.serializer.ExternalSuiteSerializer;
import dev.httpmarco.polocloud.suite.cluster.global.ExternalSuite;
import dev.httpmarco.polocloud.suite.i18n.serializer.LocalSerializer;

import java.util.Locale;

public final class GsonInstance {

    public static final Gson DEFAULT = new GsonBuilder().setPrettyPrinting()
            // if no cluster present -> no token present
            .registerTypeAdapter(ClusterConfig.class, new ClusterConfigSerializer())
            // for correct display of the language key
            .registerTypeAdapter(Locale.class, new LocalSerializer())
            // for a good redis external layout
            .registerTypeAdapter(ExternalSuite.class, new ExternalSuiteSerializer())
            .create();

}
