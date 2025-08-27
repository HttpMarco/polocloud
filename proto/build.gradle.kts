import com.google.protobuf.gradle.id

plugins {
    id("java")
    kotlin("jvm") version "2.2.10"
    id("com.google.protobuf") version "0.9.5"
}

dependencies {
    implementation(libs.bundles.proto)
    api(libs.bundles.proto)

    implementation("com.google.protobuf:protobuf-java:4.32.0")
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
    options.encoding = "UTF-8"
}

protobuf {
    protobuf {
        protoc {
            artifact = "com.google.protobuf:protoc:4.32.0"
        }
        plugins {
            id("grpc") {
                artifact = "io.grpc:protoc-gen-grpc-java:1.75.0"
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

tasks.jar {
    archiveFileName.set("polocloud-proto-$version.jar")
}

tasks.named<Copy>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

sourceSets["main"].proto.srcDir("src/main/proto")