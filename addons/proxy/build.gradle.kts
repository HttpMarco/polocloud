repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "bungeecord-repo"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    compileOnly(libs.velocity)
    compileOnly(libs.bungeecord)

    compileOnly(libs.gson)
    compileOnly(projects.sdk.sdkJava)

    compileOnly(projects.addons.api)
    runtimeOnly(projects.addons.api)
}

tasks.processResources {
    filesMatching(listOf("plugin.yml", "velocity-plugin.json")) {
        expand("version" to project.version)
    }
}