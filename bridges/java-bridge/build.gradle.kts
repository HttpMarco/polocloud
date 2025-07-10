plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "9.0.0-beta17"
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    compileOnly(project(":sdk:java"))
}

tasks.jar {
    archiveFileName.set("polocloud-java-bridge-$version.jar")
}