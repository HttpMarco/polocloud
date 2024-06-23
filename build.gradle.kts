plugins {
    alias(libs.plugins.nexus.publish)
}


allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "dev.httpmarco.polocloud"
    version = "1.0.8-SNAPSHOT"

    repositories {
        mavenCentral()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://repo.papermc.io/repository/maven-public/")
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        "annotationProcessor"(rootProject.libs.bundles.utils)
        "implementation"(rootProject.libs.bundles.utils)
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    extensions.configure<PublishingExtension> {
        publications {
            create("library", MavenPublication::class.java) {
                from(project.components.getByName("java"))

                pom {
                    name.set(project.name)
                    url.set("https://github.com/httpmarco/polocloud")
                    description.set("PoloCloud is the simplest and easiest Cloud for Minecraft")
                    licenses {
                        license {
                            name.set("Apache License")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0")
                        }
                    }
                    developers {
                        developer {
                            name.set("Mirco Lindenau")
                            email.set("mirco.lindenau@gmx.de")
                        }
                    }

                    scm {
                        url.set("https://github.com/httpmarco/polocloud")
                        connection.set("https://github.com/httpmarco/polocloud.git")
                    }
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/releases/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            username.set(System.getenv("ossrhUsername")?.toString() ?: "")
            password.set(System.getenv("ossrhPassword")?.toString() ?: "")
        }
    }
    useStaging.set(!project.rootProject.version.toString().endsWith("-SNAPSHOT"))
}