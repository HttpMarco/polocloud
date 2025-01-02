package dev.httpmarco.polocloud.common.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class GsonPool {

    public final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

}
