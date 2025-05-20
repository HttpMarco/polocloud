plugins {
    kotlin("jvm") version "2.1.20"
}

group = "dev.httpmarco"
version = "2.0.0"

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