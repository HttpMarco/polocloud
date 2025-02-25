plugins {
    id("java")
}

dependencies {
    compileOnly(project(":polocloud-suite"))
    compileOnly(libs.jline)
    compileOnly(libs.slf4j)
}

