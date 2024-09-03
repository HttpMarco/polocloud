package dev.httpmarco.polocloud.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonPoint {

    public final Gson GSON = new GsonBuilder().serializeNulls().create();

}
