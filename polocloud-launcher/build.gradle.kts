plugins {
    id("java-library")
}

dependencies {
    compileOnlyApi(libs.annotations)
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
}

tasks.jar {
    from(project(":polocloud-api").tasks.getByPath(":polocloud-api:jar"))
    from(project(":polocloud-common").tasks.getByPath(":polocloud-common:jar"))
    from(project(":polocloud-node").tasks.getByPath(":polocloud-node:jar"))
    from(project(":polocloud-daemon").tasks.getByPath(":polocloud-daemon:jar"))

    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.launcher.PolocloudBoot")
    }
    archiveFileName.set("polocloud-launcher-${version}.jar")
}

