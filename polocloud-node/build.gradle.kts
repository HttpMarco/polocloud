dependencies {
    compileOnly(project(":polocloud-api"))
    compileOnly(project(":polocloud-launcher"))
    compileOnly(libs.netline)
    compileOnly(libs.netty5)
    compileOnly(libs.jline)
    compileOnly(libs.gson)
}

tasks.jar {

    manifest {
        attributes["Main-Class"] = "dev.httpmarco.polocloud.node.boot.NodeBoot"
        attributes["Premain-Class"] = "dev.httpmarco.polocloud.node.boot.NodeBoot"
        attributes["Polocloud-Version"] = version
    }

    archiveFileName.set("polocloud-node-${version}.jar")
}

tasks.test {
    useJUnitPlatform()
}