dependencies {

}

tasks.jar {
    from(project(":polocloud-api").tasks.jar)
    from(project(":polocloud-node").tasks.jar)

    manifest {
        attributes["Polocloud-Version"] = version
        attributes["Main-Class"] = "dev.httpmarco.polocloud.launcher.boot.PoloCloudBootstrap"
    }

    archiveFileName.set("polocloud-${version}.jar")
}