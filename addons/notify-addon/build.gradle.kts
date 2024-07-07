dependencies {
    implementation(project(":api"))

    implementation(libs.bungeecord)

    implementation(libs.velocity)
    annotationProcessor(libs.velocity)

    implementation(libs.bungeeminimessage)
}
