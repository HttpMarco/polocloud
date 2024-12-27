import org.apache.tools.ant.filters.ReplaceTokens
import java.time.Instant

plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.git")
}

indra {
    javaVersions {
        target(21)
        testWith(21)
    }
}

configurations {
    testCompileClasspath {
        exclude(group = "junit")
    }
}

tasks.create<Sync>("processSources") {
    from(java.sourceSets["main"].java)
    filter(
        ReplaceTokens::class, mapOf(
            "tokens" to mapOf(
                "version" to rootProject.version,
                "gitBranch" to (indraGit.branchName() ?: "unknown"),
                "gitCommitHash" to (indraGit.commit()?.name ?: "unknown"),
                "buildTime" to Instant.now().toString(),
            )
        )
    )
    into(layout.buildDirectory.dir("src"))
}

tasks.compileJava {
    dependsOn(tasks.getByName("processSources"))
    source = tasks.getByName("processSources").outputs.files.asFileTree
}