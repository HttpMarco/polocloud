plugins {
    id("java")
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    compileOnly(project(":polocloud-api"))
}

tasks.jar {
    archiveFileName.set("polocloud-java-bridge-${version}.jar")
}
