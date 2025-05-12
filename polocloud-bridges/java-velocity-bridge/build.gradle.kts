plugins {
    id("java")
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    compileOnly(project(":polocloud-api"))
}