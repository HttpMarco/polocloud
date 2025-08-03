package dev.httpmarco.polocloud.updater

import com.google.gson.reflect.TypeToken
import dev.httpmarco.polocloud.common.json.GSON
import dev.httpmarco.polocloud.common.os.OS
import dev.httpmarco.polocloud.common.os.currentOS
import dev.httpmarco.polocloud.common.version.polocloudVersion
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.util.LinkedList

object Updater {

    fun latestVersion(): String {
        return this.availableRelease().first()
    }

    fun newVersionAvailable(): Boolean {
        return polocloudVersion() != latestVersion()
    }

    fun availableRelease(): LinkedList<String> {
        val releases = LinkedList<String>()
        val url = URI("https://api.github.com/repos/httpmarco/polocloud/tags").toURL()

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            setRequestProperty("Accept", "application/vnd.github+json")
            setRequestProperty("User-Agent", "Mozilla/5.0")

            if (responseCode == 200) {
                inputStream.bufferedReader().use { reader ->
                    val json = reader.readText()

                    val type = object : TypeToken<List<GitHubTag>>() {}.type
                    val tags = GSON.fromJson<List<GitHubTag>>(json, type)

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

    fun update(version: String = latestVersion()) {
        println("Launching updater...")

        val jarName = "polocloud-updater-3.0.0-SNAPSHOT.jar"

        val processBuilder = when (currentOS) {
            OS.WIN -> ProcessBuilder(
                "cmd.exe",
                "/c",
                "start",
                "cmd.exe",
                "/c",
                "java",
                "-jar",
                jarName,
                "--version=$version"
            )

            else -> ProcessBuilder("java", "-jar", jarName, "--version=$version")
        }

        processBuilder.directory(File("local/libs"))
        processBuilder.inheritIO()

        processBuilder.start()
    }
}