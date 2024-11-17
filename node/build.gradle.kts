dependencies {
    compileOnly(libs.jline)
    compileOnly(project(":api"))
    compileOnly(project(":launcher"))
    compileOnly(libs.netty5)
    compileOnly(libs.maven.artifact)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.httpmarco.polocloud.node.launcher.NodeLauncher"
    }
    archiveFileName.set("polocloud-node.jar")
}