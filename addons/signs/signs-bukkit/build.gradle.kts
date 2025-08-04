dependencies {
    compileOnly(libs.spigot)
    implementation(projects.addons.signs.signsJavaAbstraction)
}

tasks.processResources {
    filesMatching(listOf("plugin.yml")) {
        expand("version" to version)
    }
}