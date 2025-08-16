import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
}

val minecraftVersion = "1.21.5"
val fabricMappingsVersion = "1.21.5+build.1"
val fabricApiVersion = "0.128.2+1.21.5"

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$fabricMappingsVersion")

    modImplementation(libs.bundles.fabric)
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modCompileOnly(fabricApi.module("fabric-networking-api-v1", fabricApiVersion))
    include(fabricApi.module("fabric-api-base", fabricApiVersion))
    include(fabricApi.module("fabric-networking-api-v1", fabricApiVersion))

    compileOnly(projects.bridges.bridgeFabric)
}

loom {
    serverOnlyMinecraftJar()
}

tasks.named<RemapJarTask>("remapJar") {
    archiveFileName.set("polocloud-${project.name}-$version.jar")
}