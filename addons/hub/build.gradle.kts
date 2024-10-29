plugins {
    java
}

group = "dev.httpmarco.polocloud.addons.hub"
version = "1.0.0-alpha-1"

tasks.jar {
    archiveFileName.set("polocloud-hub-${project.version}.jar")
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    compileOnly(libs.bungeecord)
    compileOnly(project(":api"))
    compileOnly(project(":instance"))
}