plugins {
    kotlin("jvm")
}

group = "dev.httpmarco.polocloud"
version = "3.0.0.BETA"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}