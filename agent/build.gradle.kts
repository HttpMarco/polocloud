plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.bundles.proto)
    implementation(libs.grpc.netty)
    compileOnly(project(":proto"))

    implementation(libs.bundles.terminal)
    implementation(libs.bundles.runtime)
    implementation(libs.json)
    implementation(libs.gson)
    implementation(libs.jline)


    implementation(libs.bundles.confirationPool)
    compileOnly(project(":platforms"))
    compileOnly(project(":common"))
}

tasks.jar {
    archiveFileName.set("polocloud-agent-$version.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.agent.AgentBootKt");
        attributes("Premain-Class" to "dev.httpmarco.polocloud.agent.AgentBootKt")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}