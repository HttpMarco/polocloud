plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.bundles.confirationPool)

    compileOnly(libs.gson)
    compileOnly(project(":common"))
}


val copyMetadata by tasks.registering(Copy::class) {
    from("../metadata")
    into("$projectDir/src/main/resources/metadata")
}

tasks.named("processResources") {
    dependsOn(copyMetadata)
}



tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.jar {
    archiveFileName.set("polocloud-platforms-$version.jar")
}