plugins {
    kotlin("jvm") version "2.2.10"
    id("com.gradleup.shadow") version "9.0.0"
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.bundles.proto)
    implementation(libs.grpc.netty)
    compileOnly(projects.proto)
    compileOnly(projects.shared)

    implementation(libs.bundles.terminal)
    implementation(libs.bundles.runtime)
    implementation(libs.bundles.jline)

    implementation(libs.gson)
    implementation(libs.oshi)

    implementation(libs.bundles.confirationPool)
    compileOnly(projects.platforms)
    compileOnly(projects.common)
    compileOnly(projects.updater)
    compileOnly(projects.bridges.bridgeApi)
}

tasks.jar {
    // for docker images
    dependsOn(tasks.shadowJar)

    archiveFileName.set("polocloud-agent-$version.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.agent.AgentBootKt")
        attributes("Premain-Class" to "dev.httpmarco.polocloud.agent.AgentBootKt")
    }
}

tasks.shadowJar {
    archiveFileName.set("polocloud-agent-$version-all.jar")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}