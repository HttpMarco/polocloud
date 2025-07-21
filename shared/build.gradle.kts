plugins {
    kotlin("jvm") version "2.2.0"
}

dependencies {
    api(project(":proto"))
    implementation(project(":proto"))
}

kotlin {
    jvmToolchain(21)
}