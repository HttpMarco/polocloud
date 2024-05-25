dependencies {
    implementation(project(":api"))
    implementation(project(":runner"))
    implementation(libs.bundles.console)
    implementation(libs.osgan.netty)
    implementation(libs.gson)
    implementation(libs.commons)
}

tasks.jar {
    archiveFileName.set("base.jar")
}