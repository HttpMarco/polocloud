plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    compileOnly(project(":polocloud-api"))
}

tasks.shadowJar {
    archiveFileName.set("polocloud-suite-${version}.jar")
    manifest {
        attributes("Main-Class" to "dev.httpmarco.polocloud.suite.PolocloudBoot")
        attributes("Premain-Class" to "dev.httpmarco.polocloud.suite.PolocloudBoot")
    }
}
tasks.withType<ProcessResources> {
    filteringCharset = "UTF-8"
}