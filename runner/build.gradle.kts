dependencies {

}

tasks.jar {
    // add instance loader for services
    from(project(":base").tasks.jar)
    from(project(":api").tasks.jar)

    manifest {
        attributes["Premain-Class"] = "dev.httpmarco.polocloud.RunnerBootstrap"
        attributes["Main-Class"] = "dev.httpmarco.polocloud.runner.RunnerBoostrap"
    }
    archiveFileName.set("polocloud.jar")
}