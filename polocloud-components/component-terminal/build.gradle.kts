plugins {
    id("java")
}

dependencies {
    compileOnly(rootProject.project(":polocloud-suite"))
    compileOnly(libs.jline)
    compileOnly(libs.slf4j)
}

