dependencies {
    implementation(project(":polocloud-api"))
    implementation(project(":polocloud-launcher"))
}

tasks.test {
    useJUnitPlatform()
}