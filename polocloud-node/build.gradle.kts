plugins {
    id("polocloud.common")
    id("java")
    alias(libs.plugins.lombok)
}

dependencies {
    compileOnly(projects.polocloudApi)
    compileOnly(projects.polocloudCommon)

    compileOnly(libs.netline)
    compileOnly(libs.bundles.logging)

    // local testing
    runtimeOnly(projects.polocloudApi)
    runtimeOnly(projects.polocloudCommon)
    runtimeOnly(libs.bundles.logging)
}


