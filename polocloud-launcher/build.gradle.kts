plugins {
    id("java")
}

tasks.jar {
    from(
        includeLibs("api"),
        includeLibs("instance"),
        includeLibs("suite", "shadowJar"),
        includeLibs("grpc"),
        includeLibs("instance")
    )

    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.launcher.boot.PolocloudBoot")
        attributes("polocloud-version" to version)
    }

    archiveFileName.set("polocloud-launcher.jar")
}

fun includeLibs(project: String, task: String = "jar"): Task {
    return project(":polocloud-$project").tasks.getByPath(":polocloud-$project:$task")
}