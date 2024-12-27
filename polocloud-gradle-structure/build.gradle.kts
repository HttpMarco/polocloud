plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.build.indra.common)
    implementation(libs.build.indra.publishing)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}