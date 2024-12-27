plugins {
    id("polocloud.common")
    id("java-library")
    alias(libs.plugins.lombok)
}

dependencies {
    compileOnlyApi(libs.annotations)
}

tasks.jar {
    archiveFileName.set("polocloud-api.jar")
}