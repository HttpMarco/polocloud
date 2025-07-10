plugins {
    kotlin("jvm") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.json)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.jar {
    archiveFileName.set("polocloud-common-$version.jar")
}