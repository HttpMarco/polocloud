plugins {
    id("java-library")
    id("polocloud.common")
    alias(libs.plugins.lombok)
}

dependencies {
    compileOnlyApi(libs.annotations)
}

tasks.jar {
    archiveFileName.set("polocloud-api-${version}.jar")
}