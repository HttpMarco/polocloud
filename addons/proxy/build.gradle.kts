plugins {
    java
}

group = "dev.httpmarco.polocloud.addons.proxy"
version = "1.0-SNAPSHOT"

tasks.jar {
    archiveFileName.set("polocloud-proxy-${project.version}.jar")
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    compileOnly(libs.bungeecord)
}