plugins {
    kotlin("jvm") version "2.2.20"
    id("com.gradleup.shadow") version "9.2.2"
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.bundles.proto)
    implementation(libs.grpc.netty)
    implementation(projects.proto)
    implementation(projects.shared)

    implementation(libs.bundles.terminal)
    implementation(libs.bundles.runtime)
    implementation(libs.bundles.jline)

    implementation(libs.gson)
    implementation(libs.oshi)

    implementation(libs.bundles.confirationPool)
    implementation(projects.platforms)
    implementation(projects.common)
    implementation(projects.updater)
    implementation(projects.bridges.bridgeApi)
}

tasks.jar {
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