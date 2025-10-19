plugins {
    kotlin("jvm") version "2.2.20"
    id("com.gradleup.shadow") version "9.0.0"
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

    relocate("com.google.protobuf", "dev.httpmarco.polocloud.sdk.java.relocated.protobuf")
    relocate("io.grpc.protobuf", "dev.httpmarco.polocloud.sdk.java.relocated.grpc.protobuf")


    // gRPC + protobuf ServiceLoader-Einträge korrekt mergen
    mergeServiceFiles {
        include("META-INF/services/io.grpc.*")
        include("META-INF/services/com.google.protobuf.*")
        include("META-INF/services/javax.annotation.processing.Processor")
    }

    // Doppelte META-INF-Einträge ausschließen (kommt häufig durch project dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Verhindern, dass Shadow wichtige gRPC-Klassen rausschmeißt
    minimize {
        exclude(dependency("io.grpc:.*"))
        exclude(dependency("com.google.protobuf:.*"))
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}