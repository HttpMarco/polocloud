import org.gradle.kotlin.dsl.projects
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("kapt")
    id("com.gradleup.shadow") version "9.0.0-rc3"
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity)
    kapt(libs.velocity)

    implementation(libs.bstats.velocity)
    implementation(projects.sdk.sdkJava)
    implementation(projects.bridges.bridgeApi)
}

tasks.withType<ShadowJar> {
    relocate("org.bstats", "dev.httpmarco.polocloud.libs.bstats")
}