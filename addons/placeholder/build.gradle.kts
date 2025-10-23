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

    mergeServiceFiles {
        include("META-INF/services/io.grpc.*")
    }
}
