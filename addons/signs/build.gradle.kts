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
        compileOnly(parent!!.projects.sdk.sdkJava)
        compileOnly(parent!!.projects.addons.api)

        runtimeOnly(parent!!.projects.sdk.sdkJava)
        runtimeOnly(parent!!.projects.addons.api)
        testImplementation(parent!!.projects.addons.api)
        testImplementation(parent!!.projects.sdk.sdkJava)
    }
}