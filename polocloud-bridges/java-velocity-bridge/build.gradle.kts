plugins {
    id("java")
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}