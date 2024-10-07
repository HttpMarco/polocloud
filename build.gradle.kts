plugins {
    id("maven-publish")

    alias(libs.plugins.nexusPublish)
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "dev.httpmarco.polocloud.node"
    version = project.version

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

    extensions.configure<PublishingExtension> {
        publications {
            create("library", MavenPublication::class.java) {
                from(project.components.getByName("java"))

                pom {
                    name.set(project.name)
                    url.set("https://github.com/httpmarco/polocloud")
                    description.set("A simple minecraft cloud system")
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0")
                        }
                    }
                    scm {
                        url.set("https://github.com/httpmarco/polocloud")
                        connection.set("https://github.com/httpmarco/osgon.git")
                    }
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            username.set(System.getenv("ossrhUsername")?.toString() ?: "")
            password.set(System.getenv("ossrhPassword")?.toString() ?: "")
        }
    }
    useStaging.set(!project.rootProject.version.toString().endsWith("-SNAPSHOT"))
}