plugins {
    id("java-library")
}

dependencies {
    compileOnlyApi(libs.annotations)
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
}