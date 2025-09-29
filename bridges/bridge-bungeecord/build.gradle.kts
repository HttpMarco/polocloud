import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    maven {
        name = "bungeecord-repo"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
    maven {
        name = "minecraft-libraries"
        url = uri("https://libraries.minecraft.net/")
    }
}


dependencies {
    implementation(projects.bridges.bridgeApi)
    implementation(libs.bstats.bungeecord)
    compileOnly(libs.bungeecord)

}

tasks.withType<ShadowJar> {
    // Doppelte Klassen ignorieren statt Fehler
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Services richtig mergen
    mergeServiceFiles()


    exclude("META-INF/INDEX.LIST")
    exclude("META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.SF")
    exclude("io/netty/handler/ssl/OpenSsl*")

    relocate("org.bstats", "dev.httpmarco.polocloud.libs.bstats")
}

tasks.processResources {
    filesMatching(listOf("plugin.yml")) {
        expand("version" to version)
    }
}