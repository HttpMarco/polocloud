plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    compileOnly(projects.polocloudApi)
    compileOnly(projects.polocloudCommon)

    compileOnly(libs.gson)
    compileOnly(libs.netline)
    compileOnly(libs.jline.jansi)

    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)

    // the only lib that is not in the polocloud-common lib and must be compiled
    implementation(libs.log4j.sl4j.impl)
    implementation(libs.jline.jansi)
}

tasks.shadowJar {
    archiveFileName.set("polocloud-node-${version}.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.node.NodeBootContext")
        attributes("Premain-Class" to "dev.httpmarco.polocloud.node.NodeBootContext")
    }
}
tasks.withType<ProcessResources> {
    filteringCharset = "UTF-8"
}
