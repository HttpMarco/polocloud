tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.httpmarco.polocloud.instance.ClusterInstanceLauncher"
        attributes["Premain-Class"] = "dev.httpmarco.polocloud.instance.ClusterPremain"
    }
    archiveFileName.set("polocloud-instance.jar")
}

dependencies {
    compileOnly(project(":api"))
}