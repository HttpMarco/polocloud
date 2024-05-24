dependencies {
    implementation(project(":api"))
    implementation(libs.bundles.console)
    implementation(libs.osgan.netty)
    implementation(libs.gson)
    implementation(libs.commons)
}

tasks.jar {
    archiveFileName.set("base.jar")
}