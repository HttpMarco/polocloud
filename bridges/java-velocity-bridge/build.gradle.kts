plugins {
    kotlin("jvm")
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}
