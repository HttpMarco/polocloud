import com.google.gson.GsonBuilder
import org.gradle.kotlin.dsl.*

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta10"
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation(libs.bundles.logging)
    implementation(libs.gson)
    implementation(libs.jline.jansi)
    implementation(project(":polocloud-components:component-api"))

    // need grpc -> improve dependency
    implementation("com.google.guava:failureaccess:1.0.2")

    compileOnly(project(":polocloud-grpc"))
    compileOnly(project(":polocloud-api"))
    compileOnly(libs.bundles.grpc)

    compileOnly(libs.bundles.utils)
    annotationProcessor(libs.bundles.utils)

    // todo remove for terminal component
    compileOnly(libs.jline)
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

// auto generation for required suite dependencies
val generateCompileOnlyDependenciesJson by tasks.registering {
    group = "build"
    doLast {
        // we only want all compileOnly dependencies -> without polocloud api
        val dependenciesList = configurations.getByName("compileOnly").allDependencies.filter { it.name !== "polocloud-api" && it.name !== "polocloud-grpc" }.map {
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
    dependsOn(generateCompileOnlyDependenciesJson)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.6"
    }
}