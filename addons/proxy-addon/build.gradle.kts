dependencies {
    implementation(project(":api"))
    implementation(project(":instance"))

    implementation(libs.bungeecord)

    implementation(libs.velocity)
    annotationProcessor(libs.velocity)

    implementation(libs.bungeeminimessage)
}