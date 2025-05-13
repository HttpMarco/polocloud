plugins {
    id("java")
}

dependencies {
    compileOnly(libs.bundles.logging)
    compileOnly(libs.bundles.utils)
    annotationProcessor(libs.bundles.utils)
}
