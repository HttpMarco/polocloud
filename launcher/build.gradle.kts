tasks.jar {
    from(project(":api").tasks.jar)
    from(project(":node").tasks.jar)
    from(project(":instance").tasks.jar)

    manifest {
        attributes["Main-Class"] = "dev.httpmarco.polocloud.launcher.PoloCloudLauncher"
    }

    archiveFileName.set("polocloud-${version}.jar")
}