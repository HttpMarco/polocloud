plugins {
    kotlin("jvm") version "2.2.0"
}

kotlin {
    jvmToolchain(21)
}

tasks.jar {
    archiveFileName.set("polocloud-shared-$version.jar")
}