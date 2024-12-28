plugins {
    id("polocloud.common")
    id("java")
    alias(libs.plugins.lombok)
}

dependencies {
    compileOnly(projects.polocloudApi)
    compileOnly(projects.polocloudCommon)

    // local testing
    runtimeOnly(projects.polocloudApi)
    runtimeOnly(projects.polocloudCommon)
}