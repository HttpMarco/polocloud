plugins {
    id("java-library")
}

dependencies {
    compileOnlyApi(libs.annotations)
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
}

tasks.jar {
    // todo find a better way for this shit
    from(includeLibs("common"), includeLibs("api"), includeLibs("node", "shadowJar"), includeLibs("daemon"))

    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.launcher.PolocloudBoot")
        attributes("Polocloud-version" to version)
    }
    archiveFileName.set("polocloud-launcher-${version}.jar")
}

fun includeLibs(project: String, task: String = "jar") : Task {
    return project(":polocloud-$project").tasks.getByPath(":polocloud-$project:" + task)
}

