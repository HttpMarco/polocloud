package dev.httpmarco.polocloud.node.terminal.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class GitHubUtils {

    public static String CONTRIBUTORS_API = "https://api.github.com/repos/HttpMarco/Polocloud/contributors";

    @SneakyThrows
    public @NotNull List<String> fetchContributor() {
        var url = new URL(CONTRIBUTORS_API);
        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        var in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();



        var contributorsList = JsonUtils.GSON.fromJson(response.toString(), JsonArray.class);
        var contributors = new ArrayList<String>();

        for (int i = 0; i < contributorsList.size(); i++) {
            var contributor = (JsonObject) contributorsList.get(i);
            var login = contributor.get("login").getAsString();

            contributors.add(login);
        }

        return contributors;
    }

}
