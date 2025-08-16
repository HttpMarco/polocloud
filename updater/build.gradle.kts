plugins {
    kotlin("jvm") version "2.2.10"
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly(projects.common)
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "dev.httpmarco.polocloud.updater.UpdaterRuntime",
            "polocloud-version" to version
        )
    }
    archiveFileName.set("polocloud-updater-$version.jar")
}

kotlin {
    jvmToolchain(21)
}