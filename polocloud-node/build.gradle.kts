plugins {
    id("java")
}

dependencies {
    compileOnly(projects.polocloudApi)
    compileOnly(projects.polocloudCommon)

    compileOnly(libs.netline)
    compileOnly(libs.bundles.logging)

    // local testing
    runtimeOnly(projects.polocloudApi)
    runtimeOnly(projects.polocloudCommon)
    runtimeOnly(libs.bundles.logging)

    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.node.NodeBootContext")
    }
}