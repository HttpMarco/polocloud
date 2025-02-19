plugins {
    id("java")
}

dependencies {
    compileOnly(libs.versions.jline)
    compileOnly(project(":polocloud-suite"))
}

