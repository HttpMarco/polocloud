plugins {
    id("java-library")
}

dependencies {
    compileOnlyApi(libs.annotations)
}

tasks.jar {
    archiveFileName.set("polocloud-api-${version}.jar")
}