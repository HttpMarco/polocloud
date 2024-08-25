package dev.httpmarco.polocloud.node.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.node.groups.ClusterGroupTypeAdapter;
import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import dev.httpmarco.polocloud.node.platforms.PlatformVersionTypeAdapter;
import dev.httpmarco.polocloud.node.properties.PropertiesPoolSerializer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils {

    public final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(PropertiesPool.class, new PropertiesPoolSerializer())
            .registerTypeAdapter(ClusterGroup.class, new ClusterGroupTypeAdapter())
            .registerTypeAdapter(PlatformVersion.class, new PlatformVersionTypeAdapter())
            .create();

}
