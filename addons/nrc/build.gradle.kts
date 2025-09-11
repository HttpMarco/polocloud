dependencies {
    compileOnly(libs.gson)

    implementation(projects.sdk.sdkJava)
    implementation(projects.addons.api)
    implementation(libs.bundles.nrc)
}

tasks.processResources {
    filesMatching(listOf("plugin.yml", "velocity-plugin.json")) {
        expand("version" to project.version)
    }
}