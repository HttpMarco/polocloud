plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))

    compileOnly(libs.bundles.proto)
    compileOnly(libs.grpc.netty)
    compileOnly(project(":proto"))

    compileOnly(libs.bundles.terminal)
    compileOnly(libs.bundles.runtime)
    compileOnly(libs.json)
    compileOnly(libs.jline)

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