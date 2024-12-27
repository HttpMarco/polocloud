plugins {
    id("polocloud.common")
    id("java")
    alias(libs.plugins.lombok)
}

dependencies {
    compileOnly(project(":polocloud-api"))
    compileOnly(project(":polocloud-common"))

    // local testing
    runtimeOnly(project(":polocloud-api"))
    runtimeOnly(project(":polocloud-common"))
}