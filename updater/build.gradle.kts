plugins {
    kotlin("jvm") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly(projects.common)
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