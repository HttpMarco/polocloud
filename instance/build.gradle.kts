tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.httpmarco.polocloud.instance.ClusterInstanceLauncher"
    }
    archiveFileName.set("polocloud-instance.jar")
}

dependencies {
    compileOnly(project(":launcher"))
    compileOnly(project(":api"))
}