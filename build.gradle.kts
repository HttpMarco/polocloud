plugins {
    id("maven-publish")

    alias(libs.plugins.nexusPublish)
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "dev.httpmarco.polocloud.node"
    version = "1.0.5-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")

        // waterdog ref links
        maven("https://repo.waterdog.dev/releases/")
        maven("https://repo.waterdog.dev/snapshots/")
        maven("https://repo.opencollab.dev/maven-releases/")
        maven("https://repo.opencollab.dev/maven-snapshots/")

        maven {
            url = uri("http://10.114.53.2:8081/repository/maven-all/")
            isAllowInsecureProtocol = true
            credentials {
                this.username = if (hasProperty("BYTEMC_REPO_USER")) {
                    property("BYTEMC_REPO_USER").toString()
                } else {
                    System.getenv("BYTEMC_REPO_USER")
                }
                this.password = if (hasProperty("BYTEMC_REPO_PASSWORD")) {
                    property("BYTEMC_REPO_PASSWORD").toString()
                } else {
                    System.getenv("BYTEMC_REPO_PASSWORD")
                }
            }
        }
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
                    if (hasProperty("BYTEMC_REPO_URL_SNAPSHOTS")) {
                        uri(property("BYTEMC_REPO_URL_SNAPSHOTS").toString())
                    } else {
                        uri(System.getenv("BYTEMC_REPO_URL_SNAPSHOTS"))
                    }
                } else {
                    if (hasProperty("BYTEMC_REPO_URL_RELEASES")) {
                        uri(property("BYTEMC_REPO_URL_RELEASES").toString())
                    } else {
                        uri(System.getenv("BYTEMC_REPO_URL_RELEASES"))
                    }
                }
                isAllowInsecureProtocol=true
                credentials {
                    this.username = if (hasProperty("BYTEMC_REPO_USER")) {
                        property("BYTEMC_REPO_USER").toString()
                    } else {
                        System.getenv("BYTEMC_REPO_USER")
                    }
                    this.password = if (hasProperty("BYTEMC_REPO_PASSWORD")) {
                        property("BYTEMC_REPO_PASSWORD").toString()
                    } else {
                        System.getenv("BYTEMC_REPO_PASSWORD")
                    }
                }
            }
        }
    }
}