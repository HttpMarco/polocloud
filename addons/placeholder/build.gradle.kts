repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation(projects.sdk.sdkJava)
    compileOnly(libs.spigot)
    compileOnly(libs.placeholderapi)
    implementation(projects.addons.api)
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName.set("polocloud-${project.name}-$version.jar")
}