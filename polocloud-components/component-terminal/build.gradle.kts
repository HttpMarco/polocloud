plugins {
    id("java")
}

dependencies {
    implementation(project(":polocloud-api"))
    implementation(project(":polocloud-components:component-api"))
    compileOnly(libs.jline)
    compileOnly(libs.slf4j)
}

