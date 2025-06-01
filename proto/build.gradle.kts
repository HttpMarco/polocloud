import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "2.1.20"
    id("com.google.protobuf") version "0.9.5"
}

dependencies {
    implementation("io.grpc:grpc-protobuf:1.73.0")
    implementation("io.grpc:grpc-services:1.73.0")
    implementation("io.grpc:grpc-stub:1.73.0")

    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}


protobuf {
    protobuf {
        protoc {
            artifact = "com.google.protobuf:protoc:3.25.8"
        }
        plugins {
            id("grpc") {
                artifact = "io.grpc:protoc-gen-grpc-java:1.73.0"
            }
        }
        generateProtoTasks {
            ofSourceSet("main").forEach {
                it.plugins {
                    id("grpc") {}
                }
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}