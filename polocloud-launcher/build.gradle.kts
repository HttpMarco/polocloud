plugins {
    id("java-library")
    id("polocloud.common")
    alias(libs.plugins.lombok)
}

dependencies {
    compileOnly(projects.polocloudCommon)
}

tasks.jar {

    from(project(":polocloud-api").tasks.jar)
    from(project(":polocloud-common").tasks.jar)
    from(project(":polocloud-daemon").tasks.jar)

    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.launcher.PolocloudBoot")
    }
    archiveFileName.set("polocloud-launcher-${version}.jar")
}