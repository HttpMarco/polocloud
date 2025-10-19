import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.placeholderapi)

    implementation(projects.sdk.sdkJava)
    implementation(projects.addons.api)
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.shadowJar {
    archiveFileName.set("polocloud-${project.name}-$version.jar")
    // Beispiel für relocate
    relocate("io.netty", "dev.httpmarco.polocloud.shadow.netty")
    relocate("io.grpc", "dev.httpmarco.polocloud.shadow.grpc")

    // Vermeide doppelte Services
    mergeServiceFiles()

    // Optional: explizit bestimmte META-INF-Dateien ausschließen
    exclude("META-INF/LICENSE.txt")
    exclude("META-INF/io.netty.versions.properties")
}
