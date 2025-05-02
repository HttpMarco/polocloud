plugins {
    id("java")
}

dependencies {
    compileOnly(project(":polocloud-api"))
    compileOnly(libs.bundles.grpc)
}


tasks.jar {
    archiveFileName.set("polocloud-instance-${version}.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.instance.PolocloudInstanceBoot")
        attributes("Premain-Class" to "dev.httpmarco.polocloud.instance.PolocloudInstanceBoot")
    }
}