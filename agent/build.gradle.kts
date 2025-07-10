plugins {
    kotlin("jvm") version "2.2.0"
    id("com.gradleup.shadow") version "9.0.0-beta17"
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.bundles.proto)
    implementation(libs.grpc.netty)
    implementation(project(":proto"))

    implementation(libs.bundles.terminal)
    implementation(libs.bundles.runtime)
    implementation(libs.json)
    implementation(libs.jline)

    compileOnly(project(":platforms"))
    compileOnly(project(":common"))
}

sourceSets["main"].java.srcDirs(
    "../proto/build/generated/sources/proto/main/grpc",
    "../proto/build/generated/sources/proto/main/java"
)
tasks.jar {
    archiveFileName.set("polocloud-agent-$version.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.agent.AgentBootKt");
        attributes("Premain-Class" to "dev.httpmarco.polocloud.agent.AgentBootKt")
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}