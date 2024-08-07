dependencies {
    implementation(libs.spigot)

    implementation(project(":instance"))
    implementation(project(":api"))
}

tasks.jar {
    archiveFileName.set("polocloud-plugin.jar")
}
