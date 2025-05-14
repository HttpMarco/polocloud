plugins {
    id("java")
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    compileOnly(project(":polocloud-api"))

    compileOnly(libs.bundles.utils)
    annotationProcessor(libs.bundles.utils)
}

tasks.jar {
    archiveFileName.set("polocloud-java-bridge-${version}.jar")
}
