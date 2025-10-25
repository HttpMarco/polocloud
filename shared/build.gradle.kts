plugins {
    kotlin("jvm") version "2.2.21"
}

dependencies {
    api(projects.proto)
    compileOnly(libs.gson)
}

kotlin {
    jvmToolchain(21)
}