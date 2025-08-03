import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly(libs.spigot)
    implementation(libs.bstats.spigot)
}

tasks.withType<ShadowJar> {
    archiveFileName.set("polocloud-${project.name}-$version.jar")

    relocate("org.bstats", "dev.httpmarco.polocloud.libs.bstats")
}