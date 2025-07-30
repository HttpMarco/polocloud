plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(libs.json)
    testImplementation(projects.common)

    compileOnly(libs.bundles.confirationPool)

    compileOnly(projects.proto)
    compileOnly(libs.json)
    compileOnly(project(":common"))
}

val copyTasks by tasks.registering(Copy::class) {
    from("../metadata/tasks").into("$projectDir/src/main/resources/metadata/tasks")
}

val copyPlatforms by tasks.registering(Copy::class) {
    from("../metadata/platforms").into("$projectDir/src/main/resources/metadata/platforms")
}

tasks.named("processResources") {
    dependsOn(copyTasks, copyPlatforms)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.jar {
    archiveFileName.set("polocloud-platforms-$version.jar")
}