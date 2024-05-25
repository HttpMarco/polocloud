dependencies {

}

tasks.jar {
    // add instance loader for services
    from(project(":base").tasks.jar)
    from(project(":api").tasks.jar)
    from(project(":instance").tasks.jar)
    from(project(":plugin").tasks.jar)

    manifest {
        attributes["Premain-Class"] = "dev.httpmarco.polocloud.runner.RunnerBootstrap"
        attributes["Main-Class"] = "dev.httpmarco.polocloud.runner.RunnerBootstrap"
    }
    archiveFileName.set("polocloud.jar")
}