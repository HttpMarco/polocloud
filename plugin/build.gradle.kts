dependencies {
    implementation(project(":api"))

    implementation(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks.jar {
    archiveFileName.set("plugin.jar")
}