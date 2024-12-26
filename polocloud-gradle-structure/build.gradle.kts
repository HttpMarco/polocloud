plugins {
    kotlin("jvm") version "2.0.21"
}

group = "dev.httpmarco.polocloud"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}