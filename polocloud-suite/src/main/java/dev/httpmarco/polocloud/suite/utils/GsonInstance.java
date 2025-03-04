package dev.httpmarco.polocloud.suite.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.httpmarco.polocloud.suite.configuration.ClusterConfig;
import dev.httpmarco.polocloud.suite.i18n.serializer.LocalSerializer;
import dev.httpmarco.polocloud.suite.utils.serializer.SuiteConfigAestheticOptimizationSerializer;

import java.util.Locale;

public final class GsonInstance {

    public static final Gson DEFAULT = new GsonBuilder().setPrettyPrinting()
            // if no cluster present -> no token present
            .registerTypeAdapter(ClusterConfig.class, new SuiteConfigAestheticOptimizationSerializer())
            // for correct display of the language key
            .registerTypeAdapter(Locale.class, new LocalSerializer()).create();

}
