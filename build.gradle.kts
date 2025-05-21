allprojects {
    group = "dev.httpmarco.polocloud"
    version = "2.0.0"

    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_23.toString()
        targetCompatibility = JavaVersion.VERSION_23.toString()
        options.encoding = "UTF-8"
    }
}