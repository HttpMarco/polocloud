repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity)
    implementation(projects.sdk.sdkJava)
    implementation(projects.bridges.bridgeApi)
}

tasks.processResources {
    filesMatching(listOf("velocity-plugin.json")) {
        expand("version" to version)
    }
}