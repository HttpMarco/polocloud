dependencies {
    implementation(project(":runner"))
}

tasks.jar {
    archiveFileName.set("instance.jar")
}