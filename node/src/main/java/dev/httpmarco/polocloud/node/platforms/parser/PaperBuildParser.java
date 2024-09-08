package dev.httpmarco.polocloud.node.platforms.parser;

import com.google.gson.JsonObject;
import dev.httpmarco.polocloud.node.util.JsonUtils;
import dev.httpmarco.polocloud.node.util.StringUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaperBuildParser {

    private final String BUILD_URL_TEMPLATE = "https://api.papermc.io/v2/projects/%s/versions/%s/builds";
    private final String DOWNLOAD_URL_TEMPLATE = "https://api.papermc.io/v2/projects/%s/versions/%s/builds/%d/downloads/%s";

    @SneakyThrows
    public String latestBuildUrl(String version) {
        var buildUrl = String.format(BUILD_URL_TEMPLATE, "paper", version);
        var response = JsonUtils.GSON.fromJson(StringUtils.downloadStringContext(buildUrl), JsonObject.class);
        var builds = response.getAsJsonArray("builds");

        if (builds.isEmpty()) {
            return "";
        }

        var latestBuild = builds.get(builds.size() - 1).getAsJsonObject();
        var buildNumber = latestBuild.get("build").getAsInt();
        var downloads = latestBuild.getAsJsonObject("downloads");
        var application = downloads.getAsJsonObject("application");
        var fileName = application.get("name").getAsString();

        return String.format(DOWNLOAD_URL_TEMPLATE, "paper", version, buildNumber, fileName);
    }
}