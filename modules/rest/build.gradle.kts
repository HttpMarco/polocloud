dependencies {
    compileOnly(projects.shared)

    implementation(libs.gson)
    implementation(libs.javalin)

    implementation(libs.bundles.jwt)
    implementation(libs.argon2)
}
