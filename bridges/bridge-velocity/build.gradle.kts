import org.gradle.kotlin.dsl.projects
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity)
    implementation(libs.bstats.velocity)
    api(projects.bridges.bridgeApi)
}


tasks.withType<ShadowJar> {
    relocate("org.bstats", "dev.httpmarco.polocloud.libs.bstats")
}

tasks.processResources {
    filesMatching(listOf("velocity-plugin.json")) {
        expand("version" to version)
    }
}