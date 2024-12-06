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

    }

}