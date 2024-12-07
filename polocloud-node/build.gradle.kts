dependencies {
    implementation(project(":polocloud-api"))
}

tasks.test {
    useJUnitPlatform()
}