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

    //todo in libs.versions
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.24.3")
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
}

tasks.shadowJar {
    archiveFileName.set("polocloud-node-${version}.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.node.NodeBootContext")
        attributes("Premain-Class" to "dev.httpmarco.polocloud.node.NodeBootContext")
    }
}