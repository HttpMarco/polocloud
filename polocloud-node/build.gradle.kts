plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    compileOnly(projects.polocloudApi)
    compileOnly(projects.polocloudCommon)

    compileOnly(libs.gson)
    compileOnly(libs.netline)

    // local testing
    runtimeOnly(projects.polocloudApi)
    runtimeOnly(projects.polocloudCommon)
    runtimeOnly(libs.bundles.logging)

    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)

    // the only lib that is not in the polocloud-common lib and must be compiled
    implementation(libs.log4j.sl4j.impl)
}

tasks.shadowJar {
    archiveFileName.set("polocloud-node-${version}.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.node.NodeBootContext")
        attributes("Premain-Class" to "dev.httpmarco.polocloud.node.NodeBootContext")
    }
}