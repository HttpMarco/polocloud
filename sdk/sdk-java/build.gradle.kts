plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.0.0"
}

dependencies {
    api(projects.shared)
    api(projects.common)
    api(libs.grpc.netty)
    api(projects.proto)

    compileOnly(libs.gson)
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    archiveFileName = "sdk-java-3.0.0-pre.6-SNAPSHOT.jar"

    relocate("com.google.protobuf", "dev.httpmarco.polocloud.sdk.java.relocated.protobuf")
    relocate("io.grpc.protobuf", "dev.httpmarco.polocloud.sdk.java.relocated.grpc.protobuf")
    relocate("io.grpc.protobuf.lite", "dev.httpmarco.polocloud.sdk.java.relocated.grpc.protobuf.lite")
    relocate("io.grpc.stub", "dev.httpmarco.polocloud.sdk.java.relocated.grpc.stub")

    // Wichtig: gRPC & protobuf service files korrekt zusammenführen
    mergeServiceFiles {
        include("META-INF/services/io.grpc.*")
        include("META-INF/services/com.google.protobuf.*")
        include("META-INF/services/javax.annotation.processing.Processor")
    }

    // Shadow 9.x: manche .proto- oder META-INF-Files werden sonst zu aggressiv reduziert
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    minimize {
        exclude(dependency("io.grpc:.*"))
        exclude(dependency("com.google.protobuf:.*"))
    }

}


tasks.jar {
    dependsOn(tasks.shadowJar)

    // Jar selbst soll nichts erzeugen
    enabled = false
}

artifacts {
    archives(tasks.shadowJar)
}

extensions.configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.shadowJar.get()) {
                classifier = null
            }

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
