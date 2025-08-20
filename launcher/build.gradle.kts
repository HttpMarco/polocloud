import java.security.MessageDigest

plugins {
    id("java")
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
    options.encoding = "UTF-8"
}

// Configure the shadowJar task (used to create a fat jar with dependencies)
tasks.jar {
    dependsOn("buildDependencies")

    from(
        includeLibs("common"),
        includeLibs("agent"),
        includeLibs("platforms"),
        includeLibs("proto"),
        includeLibs("updater"),

        includeLibs(":bridges:bridge-velocity", "shadowJar"),
        includeLibs(":bridges:bridge-bungeecord", "shadowJar"),
        includeLibs(":bridges:bridge-gate", "shadowJar"),
        includeLibs(":bridges:bridge-waterdog", "shadowJar"),
        includeLibs(":bridges:bridge-fabric", "mergeFabricVersions")
    )

    manifest {
        attributes(
            "Main-Class" to "dev.httpmarco.polocloud.launcher.PolocloudLauncher",
            "polocloud-version" to version
        )
    }
    archiveFileName.set("polocloud-launcher.jar")
}

/**
 * Returns the output task of a specific subproject and task name.
 * By default, it retrieves the 'jar' task.
 */
fun includeLibs(project: String, task: String = "jar"): Task {
    return project(":$project").tasks.getByPath(":$project:$task")
}

/**
 * Exports all runtime dependencies from the :agent project into a JSON file,
 * including guessed Maven Central URLs.
 */
tasks.register("buildDependencies") {
    group = "build"
    description =
        "Exports runtime dependencies of :agent as a semicolon-separated file with Maven Central URLs and SHA-256 checksums."

    evaluationDependsOn(":agent")

    doLast {
        val agentProject = project(":agent")
        val mavenCentralBase = "https://repo1.maven.org/maven2"
        val runtimeClasspath = agentProject.configurations.getByName("runtimeClasspath")

        val outputFile = file("src/main/resources/dependencies.blob")
        outputFile.printWriter().use { writer ->
            if (runtimeClasspath.isCanBeResolved) {
                runtimeClasspath.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                    val group = artifact.moduleVersion.id.group
                    if (group == "dev.httpmarco.polocloud") return@forEach
                    val name = artifact.name
                    val version = artifact.moduleVersion.id.version
                    val file = artifact.file
                    val fileName = file.name
                    val groupPath = group.replace(".", "/")
                    val url = "$mavenCentralBase/$groupPath/$name/$version/$fileName"

                    val sha256 = file.inputStream().use { input ->
                        val digest = MessageDigest.getInstance("SHA-256")
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            digest.update(buffer, 0, bytesRead)
                        }
                        digest.digest().joinToString("") { "%02x".format(it) }
                    }

                    writer.println("$group;$name;$version;$fileName;$url;$sha256")
                }
            }
        }
        println("✅ Exported agent dependencies to ${outputFile.absolutePath}")
    }
}
