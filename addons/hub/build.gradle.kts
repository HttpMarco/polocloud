import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


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

    api(projects.addons.api)
    implementation(libs.bstats.bungeecord)
    implementation(libs.bstats.velocity)
}

tasks.processResources {
    filesMatching(listOf("plugin.yml", "velocity-plugin.json")) {
        expand("version" to version)
    }
}

tasks.withType<ShadowJar> {
    relocate("org.bstats", "dev.httpmarco.polocloud.libs.bstats")
}