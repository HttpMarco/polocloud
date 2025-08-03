package dev.httpmarco.polocloud.updater

import com.google.gson.reflect.TypeToken
import dev.httpmarco.polocloud.common.json.PRETTY_GSON
import dev.httpmarco.polocloud.common.version.polocloudVersion
import java.net.HttpURLConnection
import java.net.URL
import java.util.LinkedList

object Updater {

    fun latestVersion(): String {
        return this.availableRelease().first()
    }

    fun newVersionAvailable(): Boolean {
        return polocloudVersion() == latestVersion()
    }

    fun availableRelease(): LinkedList<String> {
        val releases = LinkedList<String>()
        val url = URL("https://api.github.com/repos/httpmarco/polocloud/tags")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            setRequestProperty("Accept", "application/vnd.github+json")
            setRequestProperty("User-Agent", "Mozilla/5.0")

            if (responseCode == 200) {
                inputStream.bufferedReader().use { reader ->
                    val json = reader.readText()

                    val type = object : TypeToken<List<GitHubTag>>() {}.type
                    val tags : List<GitHubTag> = PRETTY_GSON.fromJson(json, type)
                    for (tag in tags) {
                        releases.add(tag.name)
                    }
                }
            } else {
                println("Fehler beim Abrufen: HTTP $responseCode")
            }
        }

        return releases
    }
}