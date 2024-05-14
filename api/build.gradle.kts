dependencies {
    implementation(project(":runner"))
}

tasks.jar {
    archiveFileName.set("api.jar")
}