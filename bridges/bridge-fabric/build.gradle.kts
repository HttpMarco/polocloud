plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.8")
    mappings(loom.officialMojangMappings())

    implementation(projects.sdk.sdkJava)
    implementation(projects.bridges.bridgeApi)
    modImplementation(libs.fabric)
}