plugins {
    id("polocloud.base")
    id("polocloud.publishing")
}

/* Apply JUnit and setup */
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    /* JUnit */
    testImplementation(platform(libs.findLibrary("test-junit-bom").get()))
    testImplementation(libs.findLibrary("test-junit-api").get())
    testImplementation(libs.findLibrary("test-junit-engine").get())
    testImplementation(libs.findLibrary("test-junit-params").get())
    testRuntimeOnly(libs.findLibrary("test-junit-launcher").get())
}

tasks {
    test {
        useJUnitPlatform()
    }
}