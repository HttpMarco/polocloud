dependencies {
    implementation(project(":api"))
    implementation(libs.bundles.console)
}

tasks.jar {
    archiveFileName.set("base.jar")
}