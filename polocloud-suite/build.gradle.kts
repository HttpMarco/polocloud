plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    compileOnly(project(":polocloud-api"))

    implementation(libs.bundles.logging)
    implementation(libs.log4j.sl4j.impl)

    implementation(libs.gson)
    implementation(libs.jline.jansi)
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