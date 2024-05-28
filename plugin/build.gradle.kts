dependencies {
    implementation(project(":api"))
    implementation(project(":instance"))
    implementation(libs.osgan.netty)

    implementation(libs.spigot)
    implementation(libs.bungeecord)

    implementation(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks.jar {
    archiveFileName.set("plugin.jar")
}