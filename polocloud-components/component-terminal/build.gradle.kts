plugins {
    id("polocloud.common")
    id("java")
    alias(libs.plugins.lombok)
}

dependencies {
    compileOnly(libs.jline)
}