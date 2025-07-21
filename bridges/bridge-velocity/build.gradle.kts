import org.gradle.kotlin.dsl.projects

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    implementation(projects.sdk.java)
    implementation(projects.bridges.bridgeApi)
}