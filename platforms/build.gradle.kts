plugins {
    kotlin("jvm") version "2.2.20"
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(projects.common)
    testImplementation(projects.proto)

    compileOnly(libs.bundles.confirationPool)

    compileOnly(projects.proto)
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