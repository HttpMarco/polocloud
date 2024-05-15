dependencies {
    implementation(project(":api"))
    implementation(libs.bundles.console)
    implementation(libs.osgan.netty)
}

tasks.jar {
    archiveFileName.set("base.jar")
}