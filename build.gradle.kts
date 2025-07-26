plugins {
    alias(libs.plugins.nexus.publish)
}

allprojects {
    apply(plugin = "maven-publish")

    group = "dev.httpmarco.polocloud"
    version = "3.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>("library") {
                from(components["java"])

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

            username.set(System.getenv("ossrhUsername") ?: "")
            password.set(System.getenv("ossrhPassword") ?: "")
        }
    }
    useStaging.set(!project.rootProject.version.toString().endsWith("-SNAPSHOT"))
}