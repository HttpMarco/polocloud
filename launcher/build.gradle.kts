import groovy.json.JsonOutput
import java.io.File

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta17"
}

dependencies {
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
}

// Configure the shadowJar task (used to create a fat jar with dependencies)
tasks.shadowJar {
    from(
        includeLibs("common"),
        includeLibs("agent", "shadowJar"),
        includeLibs("platforms"),
        includeLibs(":bridges:java-velocity-bridge"),
        includeLibs(":bridges:java-bungeecord-bridge")
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
tasks.register("exportAgentDependenciesWithUrls") {
    group = "custom"
    description = "Exports only runtime dependencies of the :agent project as JSON with guessed Maven Central URLs."

    // Ensure the agent project is evaluated
    evaluationDependsOn(":agent")

    doLast {
        val agentProject = project(":agent")
        val dependenciesList = mutableListOf<Map<String, String>>()

        val mavenCentralBase = "https://repo1.maven.org/maven2"

        // Use runtimeClasspath to get resolved, executable dependencies
        val runtimeClasspath = agentProject.configurations.getByName("runtimeClasspath")

        if (runtimeClasspath.isCanBeResolved) {
            runtimeClasspath.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                val group = artifact.moduleVersion.id.group
                val name = artifact.name
                val version = artifact.moduleVersion.id.version
                val fileName = artifact.file.name
                val groupPath = group.replace(".", "/")
                val guessedUrl = "$mavenCentralBase/$groupPath/$name/$version/$fileName"

                // Skip internal dependencies
                if (group == "dev.httpmarco.polocloud") return@forEach

                dependenciesList.add(
                    mapOf(
                        "group" to group,
                        "name" to name,
                        "version" to version,
                        "file" to fileName,
                        "guessedUrl" to guessedUrl
                    )
                )
            }
        }

        // Pretty print the result and write to resources folder
        val json = JsonOutput.prettyPrint(JsonOutput.toJson(dependenciesList))
        val outputFile = File("${projectDir}/src/main/resources", "dependencies.json")
        outputFile.writeText(json)

        println("✅ Exported agent dependencies to ${outputFile.absolutePath}")
    }
}
