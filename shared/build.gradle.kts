plugins {
    kotlin("jvm") version "2.2.10"
}

dependencies {
    api(projects.proto)
    compileOnly(libs.gson)
    compileOnly(projects.proto)
}

kotlin {
    jvmToolchain(21)
}