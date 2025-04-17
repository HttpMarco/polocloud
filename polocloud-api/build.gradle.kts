plugins {
    id("java")
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
