dependencies {
    compileOnly(project(":polocloud-api"))
    compileOnly(project(":polocloud-launcher"))
}

tasks.jar {

    manifest {
        attributes["Main-Class"] = "dev.httpmarco.polocloud.node.NodeBoot"
    }

    archiveFileName.set("polocloud-node-${version}.jar")
}

tasks.test {
    useJUnitPlatform()
}