plugins {
    kotlin("jvm") version "2.2.10"
}

dependencies {
    testImplementation(kotlin("test"))
    api(libs.gson)
    compileOnly(projects.proto)
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