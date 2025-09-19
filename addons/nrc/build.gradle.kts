dependencies {
    compileOnly(libs.gson)

    implementation(projects.sdk.sdkJava)
    implementation(projects.addons.api)
    implementation(libs.bundles.nrc)
}

repositories {
    maven("https://repo.norisk.gg/repository/maven-public/")
}

tasks.processResources {
    filesMatching(listOf("plugin.yml", "velocity-plugin.json")) {
        expand("version" to project.version)
    }
}