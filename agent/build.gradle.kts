plugins {
    kotlin("jvm") version "2.1.20"
}

subprojects {

    apply(plugin = "kotlin")

    dependencies {
        testImplementation(kotlin("test"))
    }
    tasks.test {
        useJUnitPlatform()
    }
    kotlin {
        jvmToolchain(23)
    }
}