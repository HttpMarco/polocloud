import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly(libs.spigot)
    implementation(libs.bstats.spigot)
}

tasks.withType<ShadowJar> {
    relocate("org.bstats", "dev.httpmarco.polocloud.libs.bstats")
}

subprojects {
    apply(plugin = "com.gradleup.shadow")

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies{
        api(parent!!.projects.addons.api)
    }
}