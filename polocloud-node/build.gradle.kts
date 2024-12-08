dependencies {
    compileOnly(project(":polocloud-api"))
    compileOnly(project(":polocloud-launcher"))

    compileOnly(libs.jline)
}

tasks.jar {

    manifest {
        attributes["Main-Class"] = "dev.httpmarco.polocloud.node.boot.NodeBoot"
        attributes["Premain-Class"] = "dev.httpmarco.polocloud.node.boot.NodeBoot"
    }

    archiveFileName.set("polocloud-node-${version}.jar")
}

tasks.test {
    useJUnitPlatform()
}