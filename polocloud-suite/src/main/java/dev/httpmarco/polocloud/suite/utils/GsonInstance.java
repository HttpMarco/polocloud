package dev.httpmarco.polocloud.suite.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.httpmarco.polocloud.suite.i18n.serializer.LocalSerializer;

import java.util.Locale;

public final class GsonInstance {

    public static final Gson DEFAULT = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Locale.class, new LocalSerializer()).create();

}
