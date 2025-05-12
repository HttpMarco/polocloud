plugins {
    id("java")
}

dependencies {
    compileOnly(project(":polocloud-api"))
    compileOnly(project(":polocloud-grpc"))
    compileOnly(libs.bundles.grpc)
    compileOnly(libs.bundles.utils)
    annotationProcessor(libs.bundles.utils)

    compileOnly(libs.gson)
}


tasks.jar {
    archiveFileName.set("polocloud-instance-${version}.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.instance.PolocloudInstanceBoot")
        attributes("Premain-Class" to "dev.httpmarco.polocloud.instance.PolocloudInstanceBoot")
    }
}