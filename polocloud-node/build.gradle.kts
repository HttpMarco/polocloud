plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    compileOnly(projects.polocloudApi)
    compileOnly(projects.polocloudCommon)

    compileOnly(libs.netline)
    implementation(libs.bundles.logging)

    // local testing
    runtimeOnly(projects.polocloudApi)
    runtimeOnly(projects.polocloudCommon)
    runtimeOnly(libs.bundles.logging)

    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
}

tasks.shadowJar {
    archiveFileName.set("polocloud-node-${version}.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.node.NodeBootContext")
    }
}