import com.google.protobuf.gradle.id

val protobufProtoc by configurations.creating

plugins {
    id("java")
    id("com.google.protobuf") version "0.9.5"
}

dependencies {
    implementation(libs.bundles.proto)

    protobufProtoc("com.google.protobuf:protoc:3.25.8")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
    options.encoding = "UTF-8"
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