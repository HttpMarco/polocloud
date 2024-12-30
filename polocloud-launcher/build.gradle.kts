plugins {
    id("java-library")
    alias(libs.plugins.lombok)
}

dependencies {
    compileOnlyApi(libs.annotations)
}

tasks.jar {

    from(project(":polocloud-api").tasks.getByPath(":polocloud-api:jar"))
   // from(project(":polocloud-common").tasks.jar)
    //from(project(":polocloud-daemon").tasks.jar)

    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.launcher.PolocloudBoot")
    }
    archiveFileName.set("polocloud-launcher-${version}.jar")
}