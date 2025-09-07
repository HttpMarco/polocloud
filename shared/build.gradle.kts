plugins {
    kotlin("jvm") version "2.2.10"
}

dependencies {
    api(projects.proto)
    compileOnly(libs.gson)
}

kotlin {
    jvmToolchain(21)
}