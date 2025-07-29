plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly(projects.common)
    compileOnly(libs.json)
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveFileName.set("polocloud-updater-$version.jar")
}

kotlin {
    jvmToolchain(21)
}