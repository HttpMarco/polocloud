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
    compileOnly(libs.waterdog)
    compileOnly(libs.velocity)
    compileOnly(libs.bungeecord)
    api(projects.addons.api)
}

tasks.processResources {
    filesMatching(listOf("plugin.yml", "velocity-plugin.json", "waterdog.yml")) {
        expand("version" to project.version)
    }
}