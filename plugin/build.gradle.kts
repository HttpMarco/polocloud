dependencies {
    implementation(libs.spigot)

    implementation(project(":instance"))
    implementation(project(":api"))

    implementation(libs.velocity)
    annotationProcessor(libs.velocity)
    implementation(libs.bungeecord)
}

tasks.jar {
    archiveFileName.set("polocloud-plugin.jar")
}
