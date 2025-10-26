dependencies {
    compileOnly(projects.agent)
    compileOnly(projects.common)
    compileOnly(projects.shared)

    implementation(libs.gson)
    implementation(libs.javalin)
    implementation(libs.javalin.ssl)

    implementation(libs.bundles.jwt)
    implementation(libs.argon2)
}

tasks.jar {
    dependsOn(tasks.shadowJar)
}
