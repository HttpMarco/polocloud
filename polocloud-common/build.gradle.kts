plugins {
    id("java-library")
}

dependencies {
    compileOnlyApi(libs.annotations)
    compileOnly(libs.gson)

    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
}