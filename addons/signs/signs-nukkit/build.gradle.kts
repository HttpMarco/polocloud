import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
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
    compileOnly(libs.nukkit)
    implementation(projects.addons.signs.signsJavaAbstraction)
}

tasks.processResources {
    filesMatching(listOf("plugin.yml")) {
        expand("version" to version)
    }
}

tasks.named<ShadowJar>("shadowJar") {
    // Beispiel für relocate
    relocate("io.netty", "dev.httpmarco.polocloud.shadow.netty")
    relocate("io.grpc", "dev.httpmarco.polocloud.shadow.grpc")

    // Vermeide doppelte Services
    mergeServiceFiles()

    // Optional: explizit bestimmte META-INF-Dateien ausschließen
    exclude("META-INF/LICENSE.txt")
    exclude("META-INF/io.netty.versions.properties")
}