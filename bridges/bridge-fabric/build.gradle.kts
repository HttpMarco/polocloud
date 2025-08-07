import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("com.gradleup.shadow") version "9.0.0"
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.8")
    mappings(libs.fabric.mappings)

    implementation(projects.sdk.sdkJava)
    implementation(projects.bridges.bridgeApi)
    modImplementation(libs.bundles.fabric)
    modImplementation(fabricApi.module("fabric-networking-api-v1", "0.130.0+1.21.8"))
    include(fabricApi.module("fabric-api-base", "0.130.0+1.21.8"))
    include(fabricApi.module("fabric-networking-api-v1", "0.130.0+1.21.8"))
}

loom {
    serverOnlyMinecraftJar()
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(mutableMapOf("version" to project.version))
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("shadow")
    relocate("oshi", "dev.httpmarco.polocloud.libs.oshi")
}