plugins {
    id("java-library")
}

dependencies {
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
}