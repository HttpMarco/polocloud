import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    alias(libs.plugins.shadow)
}

group = "dev.httpmarco.polocloud.modules.rest"
version = "1.0.0-alpha"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.javalin)
    implementation(libs.bundles.jwt)
    implementation(libs.argon2)
}

tasks {
    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
        archiveFileName.set("rest-module_$version-all.jar")
    }
}

tasks {
    build {
        dependsOn("shadowJar")
    }
}
