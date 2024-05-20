package dev.httpmarco.polocloud.base.groups.platforms;

import com.google.gson.JsonObject;
import dev.httpmarco.osgan.files.json.JsonUtils;
import lombok.SneakyThrows;

import java.net.URI;

public final class PaperMCPlatform extends Platform {

    private static final String VERSION_URL = "https://api.papermc.io/v2/projects/%s";
    private static final String DOWNLOAD_URL = "https://api.papermc.io/v2/projects/%s/versions/%s/builds/%s/downloads/%s-%s-%S.jar";

    public PaperMCPlatform(String product) {
        for (var version : readPaperInformation(VERSION_URL.formatted(product)).get("versions").getAsJsonArray()) {
            possibleVersions().add(product + "-" + version.getAsString());
        }
    }


    @SneakyThrows
    private JsonObject readPaperInformation(String link) {
        var url = new URI(link).toURL();
        var stream = url.openStream();

        var context = new String(stream.readAllBytes());
        stream.close();

        return JsonUtils.getGson().fromJson(context, JsonObject.class);
    }
}
