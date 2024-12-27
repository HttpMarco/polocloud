plugins {
    alias(libs.plugins.indra.sonatype)
    alias(libs.plugins.nexusPublish)
}

group = "dev.httpmarco"
version = "1.0.0"

indraSonatype {
    useAlternateSonatypeOSSHost("s01")
}

subprojects {
    repositories {
        mavenCentral()
    }
}