plugins {
    kotlin("jvm")
}

group = "dev.httpmarco.polocloud"
version = "3.0.0-pre.4-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}