dependencies {
    compileOnly(libs.spigot)

    compileOnly(project(":instance"))
    compileOnly(project(":api"))

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    compileOnly(libs.bungeecord)
    compileOnly(libs.waterdogpe)
}

tasks.jar {
    archiveFileName.set("polocloud-plugin.jar")
}
