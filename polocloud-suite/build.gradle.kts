import com.google.gson.GsonBuilder
import org.gradle.kotlin.dsl.*
import java.io.File

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    compileOnly(project(":polocloud-api"))

    implementation(libs.bundles.logging)
    implementation(libs.log4j.sl4j.impl)

    implementation(libs.gson)
    implementation(libs.jline.jansi)
}

tasks.shadowJar {
    archiveFileName.set("polocloud-suite-${version}.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.suite.PolocloudBoot")
        attributes("Premain-Class" to "dev.httpmarco.polocloud.suite.PolocloudBoot")
    }
}

buildscript {
    dependencies {
        classpath(libs.gson)
    }
}

tasks.withType<ProcessResources> {
    filteringCharset = "UTF-8"
}

// we generate the dependencies for the polocloud suite for type safe options
val generateDependenciesJson by tasks.registering {
    group = "build"
    doLast {
        val dependenciesList = configurations.getByName("implementation").allDependencies.map {
            mapOf("group" to it.group, "name" to it.name, "version" to it.version)
        }
        val json = GsonBuilder().setPrettyPrinting().create().toJson(dependenciesList)
        val outputDir = file("src/main/resources")
        outputDir.mkdirs()
        val outputFile = File(outputDir, "dependencies.json")
        outputFile.writeText(json)
    }
}

tasks.named("processResources").configure {
    dependsOn(generateDependenciesJson)
}