package dev.httpmarco.polocloud.node.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Log4j2
public class AutoUpdater {

    private static final String REPO_URL = "https://api.github.com/repos/HttpMarco/polocloud/releases";

    public void notifyIfUpdateAvailable() {
        var release = latestRelease();
        if (release == null) {
            return;
        }

        var releaseVersion = releaseVersion(release);
        var currentVersion = System.getProperty("Polocloud-Version");

        if (!VersionVerifier.isNewerVersion(currentVersion, releaseVersion)) {
            return;
        }

        notifyUpdate(release.get("html_url").getAsString(), releaseVersion);
    }

    private void notifyUpdate(String releaseUrl, String releaseVersion) {
        log.warn(" ");
        log.warn("A new version of PoloCloud is available: &bv{}", releaseVersion);
        log.warn("You can download it via the \"node update\" command or from the official repository.");
        log.warn(releaseUrl);
        log.warn(" ");
    }

    public void update() {

    }

    public String releaseVersion(JsonObject release) {
        return release.get("tag_name").getAsString();
    }

    public JsonObject latestRelease() {
        try {
            var url = new URL(REPO_URL);
            var conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

            if (conn.getResponseCode() != 200) {
                log.error("Failed to fetch a new Update, Github request response code: {}", conn.getResponseCode());
                return null;
            }

            var reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            var response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            var releases = JsonParser.parseString(response.toString()).getAsJsonArray();
            if (releases.isEmpty()) {
                return null;
            }

            return releases.get(0).getAsJsonObject();
        } catch (IOException e) {
            log.error("Failed to fetch a new Update");
            e.printStackTrace();
            return null;
        }
    }
}
