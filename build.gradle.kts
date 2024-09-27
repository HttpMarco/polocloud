plugins {
    id("maven-publish")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "dev.httpmarco.polocloud.node"
    version = "1.0.0-alpha-2"

    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")

        // waterdog ref links
        maven("https://repo.waterdog.dev/releases/")
        maven("https://repo.waterdog.dev/snapshots/")
        maven("https://repo.opencollab.dev/maven-releases/")
        maven("https://repo.opencollab.dev/maven-snapshots/")
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        options.encoding = "UTF-8"
    }


    dependencies {
        "implementation"(rootProject.libs.lombok)
        "annotationProcessor"(rootProject.libs.lombok)
        "implementation"(rootProject.libs.annotations)
        "implementation"(rootProject.libs.log4j2)
        "implementation"(rootProject.libs.log4j2.simple)
        "implementation"(rootProject.libs.gson)
        "implementation"(rootProject.libs.osgan.netty)
    }


    if (hasProperty("PUBLISH_USERNAME") && (project.name == "api" || project.name == "instance")) {
        publishing {
            publications {
                create<MavenPublication>("mavenJava") {
                    this.groupId = group.toString()
                    this.artifactId = artifactId
                    this.version = version.toString()

                    from(components["java"])
                }
            }
            repositories {
                maven {
                    name = "polocloud"
                    url = if (version.toString().endsWith("SNAPSHOT")) {
                        uri(property("PUBLISH_URL_SNAPSHOTS").toString())
                    } else {
                        uri(property("PUBLISH_URL_RELEASES").toString())
                    }
                    isAllowInsecureProtocol=true
                    credentials {
                        username = property("PUBLISH_USERNAME").toString()
                        password = property("PUBLISH_PASSWORD").toString()
                    }
                }
            }
        }
    }
}