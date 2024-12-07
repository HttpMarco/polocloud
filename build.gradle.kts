allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "dev.httpmarco.polocloud"
    version = project.version

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        options.encoding = "UTF-8"
    }

    dependencies {
        "compileOnly"(rootProject.libs.bundles.utils)

        "testAnnotationProcessor"(rootProject.libs.lombok)
        "annotationProcessor"(rootProject.libs.lombok)

        "testImplementation"(platform(rootProject.libs.junitBom))
        "testImplementation"(rootProject.libs.bundles.testing)
    }
}
