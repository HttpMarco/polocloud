plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":agent"))

    implementation(libs.kubernetes)
    implementation(project.dependencies.platform("io.insert-koin:koin-bom:4.0.3"))
    implementation("io.insert-koin:koin-core")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}