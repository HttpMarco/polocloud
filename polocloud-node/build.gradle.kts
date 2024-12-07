dependencies {
    compileOnly(project(":polocloud-api"))
    compileOnly(project(":polocloud-launcher"))
}

tasks.jar {
    archiveFileName.set("polocloud-node-${version}.jar")
}

tasks.test {
    useJUnitPlatform()
}