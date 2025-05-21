plugins {
    kotlin("jvm") version "2.1.21"
}

group = "dev.httpmarco.polocloud.cli"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(project(":cli:terminal"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}