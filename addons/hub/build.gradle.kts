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

    maven {
        name = "waterdog-repo"
        url = uri("https://repo.waterdog.dev/main")
    }
    maven {
        name = "opencollab-snapshots"
        url = uri("https://repo.opencollab.dev/maven-snapshots/")
    }
    maven {
        name = "opencollab-releases"
        url = uri("https://repo.opencollab.dev/maven-releases/")
    }
}

dependencies {
    compileOnly(libs.velocity)
    compileOnly(libs.bungeecord)
    compileOnly(libs.waterdog)

    implementation(projects.sdk.sdkJava)
    api(projects.addons.api)
    implementation(libs.bstats.bungeecord)
    implementation(libs.bstats.velocity)
}

tasks.processResources {
    filesMatching(listOf("plugin.yml", "velocity-plugin.json", "waterdog.yml")) {
        expand("version" to version)
    }
}

tasks.withType<ShadowJar> {
    relocate("org.bstats", "dev.httpmarco.polocloud.libs.bstats")
}