plugins {
    kotlin("jvm") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.gson)
}

tasks.test {
    useJUnitPlatform()
}