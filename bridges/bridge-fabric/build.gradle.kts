import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.8")
    mappings(libs.fabric.mappings)

    implementation(projects.sdk.sdkJava)
    implementation(projects.bridges.bridgeApi)
    modCompileOnly(libs.bundles.fabric)
    modCompileOnly(fabricApi.module("fabric-networking-api-v1", "0.130.0+1.21.8"))
    include(fabricApi.module("fabric-api-base", "0.130.0+1.21.8"))
    include(fabricApi.module("fabric-networking-api-v1", "0.130.0+1.21.8"))
}

loom {
    serverOnlyMinecraftJar()
    accessWidenerPath.set(file("src/main/resources/polocloud_bridge.accesswidener"))
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(mutableMapOf("version" to project.version))
    }
}

tasks.named<RemapJarTask>("remapJar") {
    archiveFileName.set("polocloud-${project.name}-$version.jar")
}