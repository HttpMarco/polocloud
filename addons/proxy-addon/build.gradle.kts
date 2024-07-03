dependencies {
    implementation(project(":api"))
    implementation(project(":instance"))

    implementation(libs.velocity)
    annotationProcessor(libs.velocity)
}