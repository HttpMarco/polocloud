package dev.httpmarco.pololcoud.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.httpmarco.pololcoud.common.document.DocumentExclusionStrategy;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class GsonUtils {

    public static final Gson GSON = new GsonBuilder().setExclusionStrategies(new DocumentExclusionStrategy()).disableHtmlEscaping().setPrettyPrinting().create();

}
