dependencies {
    implementation(projects.addons.api)
    implementation(libs.spigot)
    implementation(libs.npc.bukkit)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    maven("https://repo.codemc.io/repository/maven-releases/") {
        mavenContent {
            includeGroup("com.github.retrooper")
        }
    }
    maven("https://repo.codemc.io/repository/maven-snapshots/") {
        mavenContent {
            includeGroup("com.github.retrooper")
        }
    }
}
